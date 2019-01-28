package gubo.http.querystring;

import static org.junit.Assert.*;
import gubo.http.querystring.QueryStringBinder.JDBCOrderBy;
import gubo.http.querystring.QueryStringBinder.JDBCWhere;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class QueryStringBinderTest {
    QueryStringBinder testee = new QueryStringBinder();
    
	@Test
	public void testJDBCWhere() throws Exception {
		
		
		Map<String, String> data = new HashMap<String, String>();
		data.put("eq__isVIP", "1");
		data.put("isnull__isVIP", "1");
		data.put("eq1__isVIP", "1");
		data.put("order_by", "isVIP desc, age");
		
		JDBCWhere jdbcWhere = testee.genJDBCWhere(data, Person.class, null);
		System.out.println("whereClause: " + jdbcWhere.whereClause);
		for (Object o : jdbcWhere.params) {
			System.out.println("\tparam: " + o);
		}
	}
	
	@Test
    public void testJDBCOrderBy() throws Exception {
        
        Map<String, String> data = new HashMap<String, String>();
        data.put("order_by", "isVIP desc, age");
        data.put("eq__isVIP", "1");
        data.put("isnull__isVIP", "1");
        data.put("eq1__isVIP", "1");
        
        JDBCOrderBy jdbcOrderBy = testee.genJDBCOrderBy(data, Person.class, null);
        System.out.println("orderByClause: " + jdbcOrderBy.getOrderByClause());
        
        assertEquals("order by isVIP desc, age", jdbcOrderBy.getOrderByClause());
        
    }
	class Animal {
		@QueryStringField()
		public String bloodColor="red";
	}
	
	class Dog extends Animal {
		@QueryStringField(name="legs_num")
		public int legsNum = 4;
	}
	@Test
	public void test_toQueryString() throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException {
		Dog dog = new Dog();
		QueryStringBinder binder = new QueryStringBinder();
		assertTrue(binder.toQueryString(dog, null).contains("bloodColor=red&"));
		assertTrue(binder.toQueryString(dog, null).contains("legs_num=4"));
	}
	
	@Test
    public void test_MapToQueryString() throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException {
        Map<String, String> data = new HashMap<String, String>();
        data.put("key1", "value1");
        QueryStringBinder binder = new QueryStringBinder();
        binder.toQueryString(data);
        assertTrue(binder.toQueryString(data).contains("key1=value1"));
    }
	
	@Test
	public void test_toHashMap() throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException {
		Dog dog = new Dog();
		QueryStringBinder binder = new QueryStringBinder();
		assertTrue(binder.toHashMap(dog, null).containsKey("bloodColor"));
		assertTrue(binder.toHashMap(dog, null).containsKey("legs_num"));
	}
	@Test
	public void test_constructBinding() throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException, InstantiationException {
		Dog dog = new Dog();
		QueryStringBinder binder = new QueryStringBinder();
		QueryStringBinder.Binding binding = binder.constructBinding(dog);
		assertEquals(2, binding._cachedParses.size());
		assertTrue(binding._cachedParses.containsKey("bloodColor"));
		assertTrue(binding._cachedParses.containsKey("legs_num"));
		assertEquals(Dog.class, binding.clazz);
	}
	
	@Test
	public void test_bind() throws Exception {
		Dog dog = new Dog();
		assertEquals(4, dog.legsNum);
		assertEquals("red", dog.bloodColor);
		QueryStringBinder binder = new QueryStringBinder();
		binder.bind("legs_num=3&bloodColor=blue", dog);
		assertEquals(3, dog.legsNum);
		assertEquals("blue", dog.bloodColor);
	}
}
