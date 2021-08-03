package springless.algo.merge.allocate.seqnum;

import java.util.concurrent.CompletableFuture;


public class SeqNumAllocateDemo {

	public static void main(String[] args) throws Exception {
		final SeqService<String> service = new SeqService<String>();
		service.setAllocator(new MemSeqAllocator<String>(345L));
		service.start();
		CompletableFuture<Long> cf1 = service.submit("key1", 2L);
		CompletableFuture<Long> cf2 = service.submit("key1", 1L);
		CompletableFuture<Long> cf3 = service.submit("key1", 21L);
		CompletableFuture<Long> cf4 = service.submit("key1", 1L);
		CompletableFuture<Long> cf5 = service.submit("key1", 1L);
		

		System.out.println("cf1.get(): " + cf1.get());
		System.out.println("cf2.get(): " + cf2.get());
		System.out.println("cf3.get(): " + cf3.get());
		System.out.println("cf4.get(): " + cf4.get());
		System.out.println("cf5.get(): " + cf5.get());
		
		
		Thread.sleep(20L);
		
		CompletableFuture<Long> cf6 = service.submit("key1", 2L);
		CompletableFuture<Long> cf7 = service.submit("key1", 1L);
		CompletableFuture<Long> cf8 = service.submit("key1", 21L);
		CompletableFuture<Long> cf9 = service.submit("key1", 1L);
		

		System.out.println("cf6.get(): " + cf6.get());
		System.out.println("cf7.get(): " + cf7.get());
		System.out.println("cf8.get(): " + cf8.get());
		System.out.println("cf9.get(): " + cf9.get());
		
		service.stop();
	}
}
