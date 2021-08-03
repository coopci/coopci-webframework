package springless.algo.merge.allocate.seqnum;

import java.util.concurrent.CompletableFuture;

public class SeqNumAllocateTask<T> {

	long count = 1L;
	T key;
	
	CompletableFuture<Long> cf;
}
