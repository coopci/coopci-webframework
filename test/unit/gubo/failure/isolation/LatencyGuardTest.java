package gubo.failure.isolation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import gubo.failure.isolation.LatencyGuard.LatencyBreakStatus;

public class LatencyGuardTest {

	
	@Test
	public void testNoDegrade() {
		
		LatencyGuard g = new LatencyGuard();
		long start = System.currentTimeMillis();
		LatencyBreakStatus s = g.begin(start, "func1");
		
		g.end(start + 1000L*5, //过了5秒
				4999, 
				"func1", "sess1", "resp1"
				);
		s = g.begin(start+ 1000L*6, "func1");
		assertEquals(LatencyBreakStatus.normal, s);
	}
	
	

	@Test
	public void testDegrade() {
		
		LatencyGuard g = new LatencyGuard();
		long start = System.currentTimeMillis();
		LatencyBreakStatus s = g.begin(start, "func1");
		
		g.end(start + 1000L*5, //过了5秒
				4999, 
				"func1", "sess1", "resp1"
				);
		
		g.end(start + 1000L*5, //过了5秒
				4999, 
				"func1", "sess2", "resp2"
				);
		s = g.begin(start+ 1000L*6, "func1");
		
		// 在6秒内有9998毫秒用在了func1上，应该degraded
		assertEquals(LatencyBreakStatus.degraded, s);
	}
	

	@Test
	public void testReset() {
		
		LatencyGuard g = new LatencyGuard();
		long start = System.currentTimeMillis();
		LatencyBreakStatus s = g.begin(start, "func1");
		
		g.end(start + 1000L*5, //过了5秒
				4999, 
				"func1", "sess1", "resp1"
				);
		
		g.end(start + 1000L*5, //过了5秒
				1000L*400, 
				"func1", "sess2", "resp2"
				);
		s = g.begin(start+ 1000L*301, "func1");
		
		// 在6秒内有9998毫秒用在了func1上，应该degraded
		assertEquals(LatencyBreakStatus.normal, s);
	}
	
	
}
