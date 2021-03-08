package gubo.failure.isolation;

import java.util.concurrent.ConcurrentHashMap;

/**
 *	最重要的是begin和end 
 * 
 **/
public class LatencyGuard {

	public static enum LatencyBreakStatus {
		
		normal, //正常，可以用真数据和流程服务。
		degraded; //服务器繁忙，应该返回错误或者cache，不应该用真数据和流程服务。
		
	}
	// ket是funcname
	ConcurrentHashMap<String, LatencyMeter> meters
	= new ConcurrentHashMap<>();
	/**
	 *	找到funcname 的 LatencyMeter
	 * 	如果没有funcname，应构造一个新 LatencyMeter
	 *  
	 *  @param ts 新构造的LatencyMeter.startTs 应赋值为ts。
	 *  
	 *  
	 **/
	private LatencyMeter getMeter(String funcname, long ts) {
		meters.putIfAbsent(funcname, new LatencyMeter(ts));
		LatencyMeter m = meters.get(funcname);
		if(ts - m.getStartTs() > 1000*300) {
			// 重新启一个LatencyMeter
			meters.put(funcname, new LatencyMeter(ts));
			m = meters.get(funcname);
		}
		
		return m;
	}
	/**
	 * 
	 * 根据功能 funcname历史上的耗时检查 funcname是不是消耗了 "过长"时间，
	 * 如果是，则返回LatencyBreakStatus.degraded,
	 * 否则返回LatencyBreakStatus.normal。
	 * 
	 * @param ts 当前时间,System.currentTimeMillis()
	 * @param funcname 将要调用的功能的名字。
	 **/
	public LatencyBreakStatus begin(long ts,
			String funcname
			) {
		// 
		LatencyMeter m =  getMeter(funcname, ts);
		if(m == null) {
			// 问题严重了，就算funcname是第一次遇到，getMeter里面应该自动加新的。
			
			return LatencyBreakStatus.normal;
		}
		if(m.getExecMS() > ts - m.getStartTs()) {
			// 因为一般是 多线程调用功能，多核执行，所以if里的判断是有可能成立的。				
			return LatencyBreakStatus.degraded;
		}
		return LatencyBreakStatus.normal;
		
	}
	
	/**
	 * 
	 * 记录一次新的调用的耗时execMs
	 * 
	 * 
	 * @param ts 当前时间,System.currentTimeMillis()
	 * @param execMs 刚刚结束的功能本次调用的耗时。
	 * @param funcname 刚刚调用结束的功能的名字。
	 * @param cacheKey 缓存刚刚调用结束的功能的结果的key。
	 * @param result 刚刚调用结束的功能的结果。
	 * 
	 **/
	public void end(long ts, 
			long execMs, 
			String funcname,
			String cacheKey,
			Object result
			) {
		
		LatencyMeter m =  getMeter(funcname, ts);
		m.accExecMS(execMs);
		
	}
	/**
	 * 拿回之前 调用end 保存的result。
	 * @param funcname 功能的名字。
	 * @param cacheKey 缓存结果的key。
	 **/
	Object getCache(String funcname, String cacheKey) {
		
		return null;
	}
	
}
