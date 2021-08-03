package springless.jdbc.mapping;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


public class StatementContext {
	/**
	 * 可以被jdcb 的Connection prepare 的
	 * 
	 * */
	String sql;
	List<ParameterSetter> setters = new LinkedList<ParameterSetter>();
	public PreparedStatement prepareStatement(Connection dbconn, Object pojo) throws SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		PreparedStatement ps = dbconn.prepareStatement(this.sql);
		
		if (setters == null || setters.size() == 0)
			return ps;
		for (ParameterSetter setter : setters) {
			setter.set(ps, pojo);
		}
		return ps;
	}
	public PreparedStatement prepareStatement(Connection dbconn, Object pojo, int opt) throws SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		PreparedStatement ps = dbconn.prepareStatement(this.sql, opt);
		
		if (setters == null || setters.size() == 0)
			return ps;
		for (ParameterSetter setter : setters) {
			setter.set(ps, pojo);
		}
		return ps;
	}
}
