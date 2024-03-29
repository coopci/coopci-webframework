package springless.db;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.HashMap;

import org.junit.Test;

import springless.db.QueryBuilder;
import springless.db.QueryBuilder.JDBCWhere;

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
		assertEquals("WHERE salary = ? AND \nage = ?", jdbcWhere.whereClause.trim());
		assertEquals(new BigDecimal("10000.00"), jdbcWhere.params[0]);
		assertEquals(11, jdbcWhere.params[1]);
	}
	
	@Test
    public void testStartswith() throws Exception {
        QueryBuilder testee = new QueryBuilder();
        HashMap<String, Object> condition = new HashMap<String, Object>();
        condition.put("startswith__name", "lalala");
        condition.put("isblank__name", "");
        condition.put("eq__age", "11");
        JDBCWhere jdbcWhere = testee.genJDBCWhere2(condition, Person.class,
                null);
        assertEquals("WHERE name like CONCAT(?, '%') AND \nname = '' AND \nage = ?", jdbcWhere.whereClause.trim());
        assertEquals("lalala", jdbcWhere.params[0]);
        assertEquals(11, jdbcWhere.params[1]);
    }
	
	@Test
    public void testContains() throws Exception {
        QueryBuilder testee = new QueryBuilder();
        HashMap<String, Object> condition = new HashMap<String, Object>();
        condition.put("contains__name", "lalala");
        condition.put("isblank__name", "");
        condition.put("eq__age", "11");
        JDBCWhere jdbcWhere = testee.genJDBCWhere2(condition, Person.class,
                null);
        assertEquals("WHERE name like CONCAT('%', ?, '%') AND \nname = '' AND \nage = ?", jdbcWhere.whereClause.trim());
        assertEquals("lalala", jdbcWhere.params[0]);
        assertEquals(11, jdbcWhere.params[1]);
    }
	
	@Test
    public void testArrayHas() throws Exception {
        QueryBuilder testee = new QueryBuilder();
        HashMap<String, Object> condition = new HashMap<String, Object>();
        condition.put("array_has__name", "('lalala', 'name2')");
        condition.put("isblank__name", "");
        condition.put("eq__age", "11");
        JDBCWhere jdbcWhere = testee.genJDBCWhere2(condition, Person.class,
                null);
        assertEquals("WHERE name in ('lalala', 'name2') AND \nname = '' AND \nage = ?", jdbcWhere.whereClause.trim());
        // assertEquals("('lalala', 'name2')", jdbcWhere.params[0]);
        assertEquals(11, jdbcWhere.params[0]);
    }
}
