package gubo.algo.merge.allocate.seqnum;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

public class SeqService<T> implements Runnable {

	ConcurrentLinkedDeque<SeqNumAllocateTask<T>> tasks = new ConcurrentLinkedDeque<SeqNumAllocateTask<T>>();

	public CompletableFuture<Long> submit(T key, long count) {
		CompletableFuture<Long> ret = new CompletableFuture<Long>();
		SeqNumAllocateTask<T> t = new SeqNumAllocateTask<T>();
		t.key = key;
		t.count = count;
		t.cf = ret;

		synchronized (this.tasks) {
			this.tasks.add(t);
			this.tasks.notifyAll();
		}
		return ret;
	}

	ThreadPoolExecutor executor;

	Thread bgThread;

	public void start() {

		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
		this.bStop = false;

		bgThread = new Thread(this);
		bgThread.setName("SeqService");
		bgThread.start();
	}

	public void stop() {
		this.bStop = true;
		if (bgThread != null) {
			try {
				bgThread.interrupt();
				bgThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			bgThread = null;
		}
		executor.shutdown();
	}

	public ISeqAllocator<T> getAllocator() {
		return allocator;
	}

	public void setAllocator(ISeqAllocator<T> allocator) {
		this.allocator = allocator;
	}

	private AtomicLong realCalls = new AtomicLong(0L);

	public AtomicLong getRealCalls() {
		return realCalls;
	}

	public void setRealCalls(AtomicLong realCalls) {
		this.realCalls = realCalls;
	}

	private ISeqAllocator<T> allocator;
	int maxBatch = 100;

	HashMap<T, MergedSeqNumAllocateTask<T>> mergedTasks = new HashMap<T, MergedSeqNumAllocateTask<T>>();
	
	void oneRound() {

		int batchSize = 0;

		
		while (!tasks.isEmpty() && batchSize < maxBatch) {
			SeqNumAllocateTask<T> t = tasks.pop();
			if (!mergedTasks.containsKey(t.key)) {
				mergedTasks.put(t.key, new MergedSeqNumAllocateTask<T>(t.key));
			}
			MergedSeqNumAllocateTask<T> mergedTask = mergedTasks.get(t.key);
			mergedTask.merge(t);
			++batchSize;
		}

		for (final Entry<T, MergedSeqNumAllocateTask<T>> entry : mergedTasks
				.entrySet()) {
			Runnable runable = new Runnable() {

				@Override
				public void run() {
					MergedSeqNumAllocateTask<T> mt = entry.getValue().makeCopy();
					entry.getValue().clear();
					
					mt.lowBound = allocator.allocate(mt.key, mt.count);

					realCalls.incrementAndGet();
					mt.maxBound = mt.lowBound + mt.count;
					mt.allocate();
					
					synchronized (tasks) {
						tasks.notifyAll();
					}
				}
			};
			executor.execute(runable);
		}
	}

	volatile boolean bStop = false;

	@Override
	public void run() {
		while (!bStop) {
			synchronized (this.tasks) {
				if (tasks.isEmpty()) {
					try {
						tasks.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						// e.printStackTrace();
						break;
					}
				}
			}
			this.oneRound();
		}
	}
}
