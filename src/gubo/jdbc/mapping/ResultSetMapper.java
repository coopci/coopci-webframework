package gubo.jdbc.mapping;

import gubo.http.querystring.QueryStringBinder;
import gubo.http.querystring.QueryStringBinder.JDBCOrderBy;
import gubo.http.querystring.QueryStringBinder.JDBCWhere;
import gubo.http.querystring.QueryStringField;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResultSetMapper<T> {
	public static Logger logger = LoggerFactory
			.getLogger(ResultSetMapper.class);

	static class Mapping {
		// key 是 表的列名， value是pojo的字段。
		ConcurrentHashMap<String, Field> colnameToField = new ConcurrentHashMap<String, Field>();

		public boolean containsCol(String col) {
			return colnameToField.containsKey(col);
		}

		public Field get(String col) {
			return colnameToField.get(col);
		}
	}

	static ConcurrentHashMap<Class<?>, Mapping> cachedMappings = new ConcurrentHashMap<Class<?>, Mapping>();

	static Mapping constructMapping(Class<?> clazz) {
		Mapping mapping = new Mapping();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class);
				String colname = column.name();
				if (colname == null || colname.length() == 0) {
					colname = field.getName();
				}
				field.setAccessible(true);
				mapping.colnameToField.put(colname, field);
			}
		}
		return mapping;
	}

	/**
	 * 如果在程序启动的时候对所有需要用的pojo类调用这个函数，那么可以 预热 cache。
	 * */
	public static Mapping getMapping(Class<?> clazz) {
		Mapping ret = cachedMappings.get(clazz);
		if (ret == null) {
			logger.debug("cachedMappings miss: {}", clazz);
			ret = constructMapping(clazz);
			cachedMappings.put(clazz, ret);
		} else {
			logger.debug("cachedMappings hit: {}", clazz);
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public List<T> mapRersultSetToObject(ResultSet rs, Class<?> outputClass) {
		if (rs == null) {
			return new ArrayList<T>();
		}
		if (!outputClass.isAnnotationPresent(Entity.class)) {
			return null;
		}
		List<T> outputList = new ArrayList<T>();
		Mapping mapping = ResultSetMapper.getMapping(outputClass);

		try {
			ResultSetMetaData rsmd = rs.getMetaData();

			int colCount = rsmd.getColumnCount();
			while (rs.next()) {
				T bean = (T) outputClass.newInstance();
				for (int _iterator = 0; _iterator < colCount; _iterator++) {
					// getting the SQL column name
					// String columnName = rsmd
					// .getColumnName(_iterator + 1);
					String columnName = rsmd.getColumnLabel(_iterator + 1);

					Object columnValue = rs.getObject(_iterator + 1);

					Field field = mapping.colnameToField.get(columnName);
					if (field == null) { // 这个resultset中的列没有对应的pojo 字段。
						continue;
					} else {
						// field.set(bean, columnValue);
						set(field, bean, columnValue);

					}
				}
				outputList.add(bean);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		if (outputList == null)
			return new ArrayList<T>();
		return outputList;
	}
	
	@SuppressWarnings("unchecked")
	public T mapRersultSetToOneObject(ResultSet rs, Class<?> outputClass) {
		if (rs == null) {
			return null;
		}
		if (!outputClass.isAnnotationPresent(Entity.class)) {
			return null;
		}
		Mapping mapping = ResultSetMapper.getMapping(outputClass);

		try {
			ResultSetMetaData rsmd = rs.getMetaData();

			int colCount = rsmd.getColumnCount();
			
			T bean = (T) outputClass.newInstance();
			for (int _iterator = 0; _iterator < colCount; _iterator++) {
				// getting the SQL column name
				// String columnName = rsmd
				// .getColumnName(_iterator + 1);
				String columnName = rsmd.getColumnLabel(_iterator + 1);

				Object columnValue = rs.getObject(_iterator + 1);

				Field field = mapping.colnameToField.get(columnName);
				if (field == null) { // 这个resultset中的列没有对应的pojo 字段。
					continue;
				} else {
					// field.set(bean, columnValue);
					set(field, bean, columnValue);

				}
			}
			return bean;
			
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	static protected void set(Field field, Object bean, Object columnValue)
			throws IllegalArgumentException, IllegalAccessException {

		if ((field.getType() == boolean.class || field.getType() == Boolean.class)
				&& columnValue instanceof Integer) {
			Integer intValue = (Integer) columnValue;
			if (intValue == 0) {
				field.set(bean, false);
			} else if (intValue == 1) {
				field.set(bean, true);
			} else {
				field.set(bean, null);
			}
		} else {
			field.set(bean, columnValue);
		}
	}

	public List<T> loadPojoList(Connection dbconn, Class<?> outputClass,
			String sql, Object... params) throws SQLException {

		PreparedStatement stmt = dbconn.prepareStatement(sql);
		int idx = 1;
		for (Object p : params) {
			stmt.setObject(idx, p);
			++idx;
		}
		ResultSet rs = stmt.executeQuery();
		List<T> pojolist = this.mapRersultSetToObject(rs, outputClass);
		return pojolist;
	}

	public T loadPojo(Connection dbconn, Class<?> outputClass, String sql,
			Object... params) throws SQLException {

		PreparedStatement stmt = dbconn.prepareStatement(sql);
		int idx = 1;
		for (Object p : params) {
			stmt.setObject(idx, p);
			++idx;
		}
		ResultSet rs = stmt.executeQuery();
		List<T> pojolist = this.mapRersultSetToObject(rs, outputClass);
		if (pojolist.isEmpty())
			return null;
		return pojolist.get(0);
	}

	public T loadPojo(DataSource ds, Class<?> outputClass, String sql,
			Object... params) throws SQLException {
		Connection dbconn = ds.getConnection();
		try {
			return this.loadPojo(dbconn, outputClass, sql, params);
		} finally {
			dbconn.close();
		}
	}

	public static <T> T loadPoJo(DataSource ds, Class<?> outputClass,
			String sql, Object... params) throws SQLException {

		Connection dbconn = ds.getConnection();
		try {
			ResultSetMapper<T> mapper = new ResultSetMapper<T>();
			T pojo = mapper.loadPojo(dbconn, outputClass, sql, params);
			return pojo;
		} finally {
			dbconn.close();
		}

	}

	public static <T> T loadPoJo(Connection dbconn, Class<?> outputClass,
			String sql, Object... params) throws SQLException {

		ResultSetMapper<T> mapper = new ResultSetMapper<T>();
		T pojo = mapper.loadPojo(dbconn, outputClass, sql, params);
		return pojo;
	}

	public static <T> T loadPoJoById(Connection dbconn, Class<?> outputClass,
			Object id) throws SQLException {
		if (!outputClass.isAnnotationPresent(Entity.class)) {
			return null;
		}
		Entity entity = outputClass.getAnnotation(Entity.class);
		String tablename = entity.name();
		String sql = "select * from " + tablename + " where id =?";
		ResultSetMapper<T> mapper = new ResultSetMapper<T>();
		T pojo = mapper.loadPojo(dbconn, outputClass, sql, id);
		return pojo;
	}

	public static <T> List<T> loadPoJoList(Connection dbconn,
			Class<?> outputClass, String sql, Object... params)
			throws SQLException {

		ResultSetMapper<T> mapper = new ResultSetMapper<T>();
		List<T> pojoList = mapper
				.loadPojoList(dbconn, outputClass, sql, params);
		return pojoList;
	}

	public static long loadLong(Connection dbconn, String sql, Object... params)
			throws SQLException {

		PreparedStatement stmt = dbconn.prepareStatement(sql);

		for (int i = 0; i < params.length; ++i) {
			stmt.setObject(i + 1, params[i]);
		}
		long ret = 0;
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			ret = rs.getLong(1);
		}
		rs.close();
		return ret;
	}

	public static BigDecimal loadBigDecimal(Connection dbconn, String sql,
			Object... params) throws SQLException {

		PreparedStatement stmt = dbconn.prepareStatement(sql);

		for (int i = 0; i < params.length; ++i) {
			stmt.setObject(i + 1, params[i]);
		}
		BigDecimal ret = new BigDecimal(0);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			ret = rs.getBigDecimal(1);
		}
		rs.close();
		return ret;
	}

	/**
	 * 
	 * @param filter
	 *            genJDBCWhere所能处理的筛选条件。也包含 order_by
	 * @param clazz
	 *            中作为筛选的字段需要用 {@link QueryStringField} 标注才行。
	 *          
	 **/
	public static <T> List<T> loadPoJoList(Connection dbconn,
			Class<?> outputClass, Map<String, String> filter) throws Exception {

		Entity entity = outputClass.getAnnotation(Entity.class);
		String tablename = entity.name();
		if (tablename == null || tablename.length() == 0) {
			tablename = outputClass.getName();
		}

		QueryStringBinder binder = new QueryStringBinder();
		JDBCWhere jdbcWhere = binder.genJDBCWhere(filter, outputClass, null);

		JDBCOrderBy jdbcOrderBy = binder.genJDBCOrderBy(filter, outputClass, null);
		
		String pagination = new PaginatorGenerator().generate(dbconn, filter);
		List<T> data = ResultSetMapper.loadPoJoList(
				dbconn,
				outputClass,
				"select * from `" + tablename + "` "
						+ jdbcWhere.getWhereClause() + " "
						+ jdbcOrderBy.getOrderByClause() + " " + pagination,
				jdbcWhere.getParams());
		return data;

	}

	static protected Object get(Field field, Object bean)
			throws IllegalArgumentException, IllegalAccessException {
		return field.get(bean);
	}

	public static void copy(Object to, Object from) throws Exception {
		Class<?> toClass = to.getClass();
		Class<?> fromClass = from.getClass();

		Mapping toMapping = ResultSetMapper.getMapping(toClass);
		Mapping fromMapping = ResultSetMapper.getMapping(fromClass);

		for (Entry<String, Field> entry : toMapping.colnameToField.entrySet()) {
			String colname = entry.getKey();
			Field fromField = fromMapping.get(colname);
			if (fromField == null) {
				continue;
			}
			Object columnValue = get(fromField, from);
			set(entry.getValue(), to, columnValue);
		}
	}

}
