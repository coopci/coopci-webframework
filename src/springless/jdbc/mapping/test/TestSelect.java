package springless.jdbc.mapping.test;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import springless.jdbc.mapping.ResultSetMapper;


public class TestSelect {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		// 构建影射关系，这步可以这样手工调用，也可以让 ResultSetMapper 自动调用。
		ResultSetMapper.getMapping(A.class);
		
		
		lengthyTest();
		
		shortTest();
		return;
	}
	
	public static void lengthyTest() throws SQLException {

		Connection dbconn = DBUtils.getHikariConnection();
		
		String sql = "select * from table1 where id>? and id < ? ";
		PreparedStatement stmt = dbconn.prepareStatement(sql);
		stmt.setLong(1, 0L);
		stmt.setLong(2, 1000L);
		ResultSet rs = stmt.executeQuery();
		
		ResultSetMapper<A> mapper = new ResultSetMapper<A>();
		List<A> pojolist = mapper.mapRersultSetToObject(rs, A.class);
		A.print(pojolist);
	}
	
	public static void shortTest() throws SQLException {
		Connection dbconn = DBUtils.getHikariConnection();
		String sql = "select * from table1 where id>? and id < ? ";
		ResultSetMapper<A> mapper = new ResultSetMapper<A>();
		List<A> pojolist = mapper.loadPojoList(dbconn, A.class, sql, 3L, 1000L);
		
		A.print(pojolist);	
	}
	
}
