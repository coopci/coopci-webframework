package springless.algo.merge.allocate.seqnum;

import java.util.LinkedList;



public class MergedSeqNumAllocateTask<T>  {

	long count = 0L;
	T key;
	
	long lowBound = -1L;
	long maxBound = -1L;
	
	public MergedSeqNumAllocateTask(T k) {
		this.key = k;
	}
	LinkedList<SeqNumAllocateTask<T>> tasks = new LinkedList<SeqNumAllocateTask<T>>();
	
	
	public void merge(SeqNumAllocateTask<T> task) {
		this.count += task.count;
		tasks.add(task);
	}

	public void allocate() {
		
		long pos = lowBound;
		for (SeqNumAllocateTask<T> task : tasks) {
			if (pos + task.count <= maxBound) {
				task.cf.complete(pos);
				pos += task.count;
			} else {
				task.cf.complete(-1L);
			}
		}
	}
	
	public MergedSeqNumAllocateTask<T> makeCopy() {
		MergedSeqNumAllocateTask<T> copy = new MergedSeqNumAllocateTask<T>(this.key);
		copy.count = this.count;
		copy.lowBound = this.lowBound;
		copy.maxBound = this.maxBound;
		copy.tasks = tasks;
		return copy;
	}
	
	public void clear() {
		this.count = 0L;
		this.lowBound = -1L;
		this.maxBound = -1L;
		this.tasks = new LinkedList<SeqNumAllocateTask<T>>();
	}

}
