package gubo.datatable.serverprocess;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import springless.datatable.serverprocess.SqlOrderBy;

public class SqlOrderByTest {

	@Test
	public void t1() {
		
		String[] columnnames = new String[]{"col0","col1","col2","col3"};
		Map<String, String> dtParams = new HashMap<String, String>();
		
			
		dtParams.put("order[0][column]", "3");
		dtParams.put("order[0][dir]", "desc");
		
		dtParams.put("order[1][column]", "2");
		
		
		
		String orderby = SqlOrderBy.toSqlOrderBy(dtParams, columnnames, "mysql");
		
		System.out.println(orderby);
	}
}
