package gubo.db;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.HashMap;

import gubo.db.QueryBuilder.JDBCWhere;

import org.junit.Test;

public class QueryBuilderTest {

	
	@Test
	public void testGenJDBCWhereStringValues() throws Exception {
		QueryBuilder testee = new QueryBuilder();
		HashMap<String, String> condition = new HashMap<String, String>();
		condition.put("eq__salary", "10000.00");
		JDBCWhere jdbcWhere = testee.genJDBCWhere(condition, Person.class,
				null);
		assertEquals("WHERE salary = ?", jdbcWhere.whereClause.trim());
		assertEquals(new BigDecimal("10000.00"), jdbcWhere.params[0]);
	}
	
	@Test
	public void testGenJDBCWhereObjectValues() throws Exception {
		QueryBuilder testee = new QueryBuilder();
		HashMap<String, Object> condition = new HashMap<String, Object>();
		condition.put("eq__salary", new BigDecimal("10000.00"));
		condition.put("eq__age", "11");
		JDBCWhere jdbcWhere = testee.genJDBCWhere2(condition, Person.class,
				null);
		assertEquals("WHERE salary = ? AND age = ?", jdbcWhere.whereClause.trim());
		assertEquals(new BigDecimal("10000.00"), jdbcWhere.params[0]);
		assertEquals(11, jdbcWhere.params[1]);
	}
}
