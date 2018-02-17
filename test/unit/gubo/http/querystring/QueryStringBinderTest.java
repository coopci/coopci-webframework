package gubo.http.querystring;

import gubo.http.querystring.QueryStringBinder.JDBCWhere;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class QueryStringBinderTest {

	@Test
	public void testJDBCWhere() throws Exception {
		
		QueryStringBinder binder = new QueryStringBinder();
		Map<String, String> data = new HashMap<String, String>();
		data.put("eq__isVIP", "1");
		data.put("isnull__isVIP", "1");
		data.put("eq1__isVIP", "1");
		
		JDBCWhere jdbcWhere = binder.genJDBCWhere(data, Person.class, null);
		System.out.println("whereClause: " + jdbcWhere.whereClause);
		for (Object o : jdbcWhere.params) {
			System.out.println("\tparam: " + o);
		}
	}
}
