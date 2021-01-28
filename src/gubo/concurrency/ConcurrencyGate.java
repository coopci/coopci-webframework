package gubo.concurrency;

import java.util.concurrent.Semaphore;



/**
   * 封装Semaphore，在AutoCloseable.close里面release。
   *而且可以通过ConcurrencyGate.setPermits 动态设置个数。
 * ConcurrencyGate cg = ...
 *try(Ticket t = cg.accquire()){
 *	...
 *}
 **/
public class ConcurrencyGate {
	public static class Ticket implements AutoCloseable {
		private final Semaphore sem;
		private final int n;
		Ticket(Semaphore sem, int n) {
			this.sem = sem;
			this.n = n;
		}
		@Override
		public void close() throws Exception {
			if(this.sem == null) {
				return;
			}
			// TODO Auto-generated method stub
			this.sem.release(n);
		}
		
		
		public int availablePermits() {
			return this.sem.availablePermits();
		}
	}
	public ConcurrencyGate(int permits) {
		this.permits = permits;
		sem = new Semaphore(permits);
		
	}
	public Ticket accquire(int n) throws InterruptedException {
		Semaphore sem = this.sem;
		Ticket ret = new Ticket(this.sem, n);
		sem.acquire(n);
		return ret;
	}
	
	public Ticket accquire() throws InterruptedException {
		return this.accquire(1);
	}
	
	volatile int permits = 5;
	volatile Semaphore sem = new Semaphore(permits);
	
	public void setPermits(int permits) {
		this.permits = permits;
		sem = new Semaphore(permits);
	}
	public int getPermits() {
		return this.permits;
	}
	
	
}
