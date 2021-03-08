package gubo.failure.isolation;

import java.util.concurrent.atomic.AtomicLong;

public class LatencyMeter {

	// 开始记录的时间。
	AtomicLong startTs;
	
	// 有记录以来累计的耗时。
	AtomicLong execMS = new AtomicLong(0L);
	
	public LatencyMeter(long ts) {
		startTs = new AtomicLong(ts);
	}
	
	public long getStartTs() {
		return this.startTs.longValue();
	}
	
	public long getExecMS() {
		return this.execMS.longValue();
	}
	public void accExecMS(long execMs){
		
		this.execMS.addAndGet(execMs);
	}
	
}
