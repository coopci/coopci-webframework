package gubo.jdbc.mapping;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ResultSetMapper<T> {
	public static Logger logger = LoggerFactory
			.getLogger(ResultSetMapper.class);
	
	static class Mapping {
		// key 是 表的列名， value是pojo的字段。
		ConcurrentHashMap<String, Field> colnameToField = new ConcurrentHashMap<String, Field>(); 
	}
	
	static ConcurrentHashMap<Class<?>, Mapping> cachedMappings = new ConcurrentHashMap<Class<?>, Mapping>();
	
	static Mapping constructMapping(Class<?> clazz) {
		Mapping mapping = new Mapping();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(Column.class)) {
				Column column = field
						.getAnnotation(Column.class);
				String colname = column.name();
				if (colname == null || colname.length() == 0) {
					colname = field.getName();
				}
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
			cachedMappings.put(clazz,  ret);
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
					//String columnName = rsmd
					//		.getColumnName(_iterator + 1);
					String columnName = rsmd
							.getColumnLabel(_iterator + 1);
					
					Object columnValue = rs.getObject(_iterator + 1);
					
					Field field = mapping.colnameToField.get(columnName);
					if (field == null) { // 这个resultset中的列没有对应的pojo 字段。
						continue; 
					} else {
						// field.set(bean, columnValue);
						set(field,  bean,  columnValue);
						
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

	protected void set(Field field, Object bean, Object columnValue) throws IllegalArgumentException, IllegalAccessException {
		
		if ( (field.getType() == boolean.class 
				||
				field.getType() == Boolean.class
			)
				&& columnValue instanceof Integer) {
			Integer intValue = (Integer)columnValue;
			if (intValue == 0) {
				field.set(bean, false);
			} else if (intValue == 1){
				field.set(bean, true);
			} else {
				field.set(bean, null);
			}
		} else {
			field.set(bean, columnValue);	
		}
	}
	
	public List<T> loadPojoList(Connection dbconn, Class<?> outputClass, String sql, Object ...params) throws SQLException {
		
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
	
	public T loadPojo(Connection dbconn, Class<?> outputClass, String sql, Object ...params) throws SQLException {
		
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
}
