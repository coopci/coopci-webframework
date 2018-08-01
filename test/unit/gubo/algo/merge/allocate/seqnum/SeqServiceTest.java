package gubo.algo.merge.allocate.seqnum;

import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.junit.Test;

public class SeqServiceTest {

	@Test
	public void f() throws InterruptedException, ExecutionException {
		int n = 100;
		final SeqService<String> service = new SeqService<String>();
		service.setAllocator(new MemSeqAllocator<String>(345L));
		service.start();

		for (int i = 0; i < n; ++i) {
			CompletableFuture<Long> cf = service.submit("key1", 1L);
			cf.get();
			// System.out.println("cf.get(): " + cf.get());
		}

		service.stop();
	}

	@Test
	public void f2() throws InterruptedException, ExecutionException {
		int n = 1000;
		final SeqService<String> service = new SeqService<String>();
		service.setAllocator(new MemSeqAllocator<String>(345L));
		service.start();
		LinkedList<CompletableFuture<Long>> cfList = new LinkedList<CompletableFuture<Long>>();
		for (int i = 0; i < n; ++i) {
			CompletableFuture<Long> cf = service.submit("key1", 1L);
			cfList.add(cf);
		}
		for (CompletableFuture<Long> cf : cfList) {
			cf.get();
			System.out.println("cf.get(): " + cf.get());
		}

		service.stop();
		System.out.println("service.getRealCalls(): " + service.getRealCalls());
	}

	@Test
	public void f3() throws InterruptedException, ExecutionException {
		int n = 1000;
		final SeqService<String> service = new SeqService<String>();
		service.setAllocator(new MemSeqAllocator<String>(345L));
		service.start();
		LinkedList<Thread> threads = new LinkedList<Thread>();
		for (int i = 0; i < n; ++i) {

			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					CompletableFuture<Long> cf = service.submit("key1", 1L);
					try {
						System.out.println("cf.get(): " + cf.get());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			threads.add(t);
			t.start();

		}
		for (Thread thread : threads) {
			thread.join();
		}

		service.stop();
	}

	@Test
	public void f4() throws InterruptedException, ExecutionException {
		int n = 10000;
		CountDownLatch latch = new CountDownLatch(n);
		final SeqService<String> service = new SeqService<String>();
		service.setAllocator(new MemSeqAllocator<String>(345L));
		service.start();

		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(400);
		for (int i = 0; i < n; ++i) {

			executor.execute(new Runnable() {

				@Override
				public void run() {
					CompletableFuture<Long> cf = service.submit("key1", 1L);
					
					// latch.countDown();
					
					try {
						System.out.println("cf.get(): " + cf.get());
						latch.countDown();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});

		}
		
		latch.await();
		
		service.stop();
		System.out.println("service.getRealCalls(): " + service.getRealCalls());
		
		
	}

}
