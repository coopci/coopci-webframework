package springless.http.querystring.demo;

import java.util.Date;

import springless.http.querystring.QueryStringField;

public class Person {
	
	
	@QueryStringField()
	public String name;
	
	
	@QueryStringField(
			// deserializer=LongFieldParser.class, 
			ignoreMalFormat=false,
			name="register_time"
			)
	public Date registerTime;
	
	@QueryStringField(
			//deserializer=LongFieldParser.class
			)
	public long age;
	
	@QueryStringField(
			// ignoreMalFormat=false
			)
	public boolean isVIP;
	
	@QueryStringField()
	public double height;
	
	
	@QueryStringField(
			deserializer=RomanIntegerFieldParser.class
			)
	public int tier;
}
