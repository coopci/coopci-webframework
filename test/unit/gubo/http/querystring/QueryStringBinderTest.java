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
		@QueryStringField()
		public int logsNum = 4;
	}
	@Test
	public void test_toQueryString() throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException {
		Dog dog = new Dog();
		QueryStringBinder binder = new QueryStringBinder();
		assertTrue(binder.toQueryString(dog, null).contains("bloodColor=red&"));
		assertTrue(binder.toQueryString(dog, null).contains("logsNum=4&"));
	}
	
	
}
