package learn.statsd;

import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;

public class SimpleMetrics {
	private static final StatsDClient statsd = new NonBlockingStatsDClient("my.prefix", "localhost", 8125);

	public static void main(String args[]) throws InterruptedException {
			System.out.println(statsd.getClass().getName());
			System.out.println(statsd.getClass().getCanonicalName());
			System.out.println(statsd.getClass().getSimpleName());
		    statsd.incrementCounter("bar");
		    statsd.recordGaugeValue("baz", 100);
		    statsd.recordExecutionTime("bag", 25);
		    statsd.recordSetEvent("qux", "one");
		    statsd.gauge("gauge1", 111);
		    Thread.sleep(2000);
	}
}
