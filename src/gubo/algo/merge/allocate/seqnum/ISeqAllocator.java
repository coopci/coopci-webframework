package gubo.algo.merge.allocate.seqnum;

public interface ISeqAllocator<T> {
	public long allocate(T key, long count);
}
