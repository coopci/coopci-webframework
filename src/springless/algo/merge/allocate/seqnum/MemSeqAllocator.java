package springless.algo.merge.allocate.seqnum;

import java.util.concurrent.atomic.AtomicLong;

public class MemSeqAllocator<T> implements ISeqAllocator<T> {

	AtomicLong seq = new AtomicLong(0L); 
	@Override
	public long allocate(T key, long count) {
		try {
			Thread.sleep(10L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return seq.getAndAdd(count);
	}
	public MemSeqAllocator(long init) {
		
		this.seq.set(init);
	}
}
