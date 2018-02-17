package gubo.http.querystring.demo;

import gubo.http.querystring.QueryStringBinder;

import java.util.Date;

public class Hello {

	public static void benchmark(long runs) throws Exception{
		String qs = "name=gubo&register_time=2017-10-23 10:00:09&a=b&age=10&isVIP=1&height=1.87&tier=VI";
		Person p = new Person();
		
		QueryStringBinder binder = new QueryStringBinder();
		
		Date before = new Date();
		for (long i = 0; i < runs; ++i) {
			binder.bind(qs, p);	
		}
		Date after = new Date();
		
		System.out.println("benchmark took " + (after.getTime() - before.getTime()) + "ms: " + runs + " runs");
	}
	
	public static void main(String[] args) throws Exception {
	
		Person p = new Person();
		
		QueryStringBinder binder = new QueryStringBinder();
		binder.bind("name=gubo&register_time=2017-10-23 10:00:09&a=b&age=10&isVIP=1&height=1.87&tier=VI", p);
		System.out.println("p.name=" + p.name);
		System.out.println("p.registerTime=" + p.registerTime );
		System.out.println("p.age=" + p.age );
		System.out.println("p.height=" + p.height );
		System.out.println("p.tier=" + p.tier );
		
		benchmark(1000);
		benchmark(10000);
		benchmark(100000);
		// benchmark(1000000);
	}
	
	
	
	
}
