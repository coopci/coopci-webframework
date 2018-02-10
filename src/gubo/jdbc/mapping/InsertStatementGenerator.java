package gubo.jdbc.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * 生成 update 的jdbc statement。
 * 
 * */
public class InsertStatementGenerator {
	public static Logger logger = LoggerFactory
			.getLogger(InsertStatementGenerator.class);
	
	public static ConcurrentHashMap<Class<?>, StatementContext> cachedInsertStatementContext = new ConcurrentHashMap<Class<?>, StatementContext>();
	
	public StatementContext constructInsert(Class<?> clazz) throws NoSuchMethodException, SecurityException {
		// Class<?> clazz = pojo.getClass();
		if (!clazz.isAnnotationPresent(Entity.class)) {
			return null;
		}
		Entity entity = clazz.getAnnotation(Entity.class);
		
		
		String tablename = entity.name();
		if (tablename == null || tablename.length() == 0) {
			tablename = clazz.getName();
		}
		
		StatementContext ret = new StatementContext();
		Field[] fields = clazz.getDeclaredFields();
		int index = 1;
		for (Field field : fields) {
			if (field.isAnnotationPresent(Column.class)) {
				if (field.isAnnotationPresent(GeneratedValue.class)) {
					continue;  // insert 的时候不  insert GeneratedValue 的字段。
				}
				
				Column column = field.getAnnotation(Column.class);
				
				if (column.insertable() == false) {
					continue;
				}
				
				String columnName = column.name();
				if (columnName == null || columnName.length() == 0) {
					columnName = field.getName();
				}
				
				ParameterSetter setter = new ParameterSetter();
				setter.index = index;
				setter.pojoField = field;
				setter.columnName = columnName;
				// setter.preparedStatementSetMethod = psClazz.getMethod("setObject", int.class, Object.class);
				setter.preparedStatementSetMethod = PreparedStatement.class.getMethod("setObject", int.class, Object.class);
				++index;
				ret.setters.add(setter);
			}
		}
		
		int setters = ret.setters.size();
		int nth = 0;
		StringBuilder sqlBuilder = new StringBuilder(); 
		sqlBuilder.append("insert into `");
		sqlBuilder.append(tablename);
		sqlBuilder.append("` (" );
		if (setters > 0) {
			sqlBuilder.append("`" );
			sqlBuilder.append(ret.setters.get(0).columnName);
			sqlBuilder.append("`" );
		}
		for (nth = 1; nth < setters; nth++) {
			sqlBuilder.append(", `" );
			sqlBuilder.append(ret.setters.get(nth).columnName);
			sqlBuilder.append("`" );
		}
		
		sqlBuilder.append(" )" );
		sqlBuilder.append(" values (" );
		if (setters > 0) {
			sqlBuilder.append("?" );
		}
		for (nth = 1; nth < setters; nth++) {
			sqlBuilder.append(", ?" );
		}
		sqlBuilder.append("); " );
		ret.sql = sqlBuilder.toString();
		return ret;
	}
	
	public StatementContext getInsertStatementContext(Class<?> clazz) throws NoSuchMethodException, SecurityException {
		StatementContext ret = cachedInsertStatementContext.get(clazz);
		if (ret == null) {
			logger.debug("cachedInsertStatementContext miss: {}", clazz);
			ret = constructInsert(clazz);
			cachedInsertStatementContext.put(clazz,  ret);
		} else {
			
			logger.debug("cachedInsertStatementContext hit: {}", clazz);
		}
		return ret;
	}
	public PreparedStatement prepareInsertStatement(Connection dbconn, Object pojo, int opt) throws SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		StatementContext ctx = this.getInsertStatementContext(pojo.getClass());
		PreparedStatement stmt = ctx.prepareStatement(dbconn, pojo, opt);
		return stmt;
	}

	public PreparedStatement prepareInsertStatement(Connection dbconn, Object pojo) throws SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		StatementContext ctx = this.getInsertStatementContext(pojo.getClass());
		PreparedStatement stmt = ctx.prepareStatement(dbconn, pojo);
		return stmt;
	}
	
	
}
