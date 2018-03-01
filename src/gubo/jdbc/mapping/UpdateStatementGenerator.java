package gubo.jdbc.mapping;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 生成 update 的jdbc statement。
 * 
 * */
public class UpdateStatementGenerator {
	public static Logger logger = LoggerFactory
			.getLogger(UpdateStatementGenerator.class);

	public static ConcurrentHashMap<Class<?>, StatementContext> cachedUpdateStatementContext = new ConcurrentHashMap<Class<?>, StatementContext>();

	public StatementContext constructUpdate(Class<?> clazz)
			throws NoSuchMethodException, SecurityException {
		
		return constructUpdate(clazz, null);
		
	}
	public StatementContext constructUpdate(Class<?> clazz, Set<String> allowedCols)
			throws NoSuchMethodException, SecurityException {
		// 返回 ret 的 setters 的前面放要set的字段，后面放where 后面的条件。
		// 所有 带@Column 和 @Id 的字段作为where的条件。
		// 所有 带@Column 不带@Id 并且 @Column 的 updatable() == true 的字段作为被set的字段。
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

		LinkedList<Field> values = new LinkedList<Field>();
		LinkedList<Field> ids = new LinkedList<Field>();

		for (Field field : fields) {
			if (field.isAnnotationPresent(Column.class)) {
				if (field.isAnnotationPresent(Id.class)) {
					// 用这些字段 作为where的条件。
					ids.addLast(field);
				} else {
					values.addLast(field);
				}
			}
		}

		int index = 1;
		for (Field field : values) {
			Column column = field.getAnnotation(Column.class);
			if (column.updatable() == false) {
				continue;
			}
			
			String columnName = column.name();
			if (columnName == null || columnName.length() == 0) {
				columnName = field.getName();
			}
			if (allowedCols != null && 
					(!allowedCols.contains(columnName))
					) {
				continue;
			}
			
			ParameterSetter setter = new ParameterSetter();
			setter.index = index;
			setter.pojoField = field;
			setter.columnName = columnName;
			setter.preparedStatementSetMethod = PreparedStatement.class
					.getMethod("setObject", int.class, Object.class);
			ret.setters.add(setter);
			++index;
		}

		int valueSetters = index - 1;
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("UPDATE `");
		sqlBuilder.append(tablename);
		sqlBuilder.append("` set ");
		if (valueSetters > 0) {
			sqlBuilder.append("`");
			sqlBuilder.append(ret.setters.get(0).columnName);
			sqlBuilder.append("` = ? ");
		}
		for (int nth = 1; nth < valueSetters; nth++) {
			sqlBuilder.append(", `");
			sqlBuilder.append(ret.setters.get(nth).columnName);
			sqlBuilder.append("` = ?");
		}
		sqlBuilder.append(" WHERE ");

		for (Field field : ids) {
			Column column = field.getAnnotation(Column.class);

			String columnName = column.name();
			if (columnName == null || columnName.length() == 0) {
				columnName = field.getName();
			}
			ParameterSetter setter = new ParameterSetter();
			setter.index = index;
			setter.pojoField = field;
			setter.columnName = columnName;
			setter.preparedStatementSetMethod = PreparedStatement.class
					.getMethod("setObject", int.class, Object.class);
			ret.setters.add(setter);
			++index;
		}

		int idSetters = index - 1 - valueSetters;
		if (idSetters > 0) {
			sqlBuilder.append("`");
			sqlBuilder.append(ret.setters.get(valueSetters).columnName);
			sqlBuilder.append("` = ? ");
		}
		for (int nth = 1; nth < idSetters; nth++) {
			sqlBuilder.append("AND `");
			sqlBuilder.append(ret.setters.get(valueSetters + nth).columnName);
			sqlBuilder.append("` = ?");
		}

		ret.sql = sqlBuilder.toString();
		return ret;
	}

	public StatementContext getUpdateStatementContext(Class<?> clazz)
			throws NoSuchMethodException, SecurityException {
		StatementContext ret = cachedUpdateStatementContext.get(clazz);
		if (ret == null) {
			logger.debug("cachedUpdateStatementContext miss: {}", clazz);
			ret = constructUpdate(clazz);
			cachedUpdateStatementContext.put(clazz, ret);
		} else {

			logger.debug("cachedUpdateStatementContext hit: {}", clazz);
		}
		return ret;
	}

	public PreparedStatement prepareUpdateStatement(Connection dbconn,
			Object pojo, int opt) throws SQLException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		StatementContext ctx = this.getUpdateStatementContext(pojo.getClass());
		PreparedStatement stmt = ctx.prepareStatement(dbconn, pojo, opt);
		return stmt;
	}

	public PreparedStatement prepareUpdateStatement(Connection dbconn,
			Object pojo) throws SQLException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		StatementContext ctx = this.getUpdateStatementContext(pojo.getClass());
		PreparedStatement stmt = ctx.prepareStatement(dbconn, pojo);
		return stmt;
	}

	public static void update(Connection dbconn, Object pojo)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, SQLException {

		UpdateStatementGenerator generator = new UpdateStatementGenerator();
		PreparedStatement stmt = generator.prepareUpdateStatement(dbconn, pojo,
				Statement.RETURN_GENERATED_KEYS);
		stmt.executeUpdate();
	}

	
	public static void update(Connection dbconn, Object pojo, Set<String> allowedCols)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException,
			SecurityException, SQLException {

		UpdateStatementGenerator generator = new UpdateStatementGenerator();
		PreparedStatement stmt = generator.prepareUpdateStatement(dbconn, pojo,allowedCols,
				Statement.RETURN_GENERATED_KEYS);
		stmt.executeUpdate();
	}
	
	
	public StatementContext getUpdateStatementContext(Class<?> clazz,
			Set<String> allowedCols) throws NoSuchMethodException,
			SecurityException {
		StatementContext ret = cachedUpdateStatementContext.get(clazz);
		ret = constructUpdate(clazz, allowedCols);
		return ret;
	}

	public PreparedStatement prepareUpdateStatement(Connection dbconn,
			Object pojo, Set<String> allowedCols, int opt)
			throws SQLException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		StatementContext ctx = this.getUpdateStatementContext(pojo.getClass(), allowedCols);
		PreparedStatement stmt = ctx.prepareStatement(dbconn, pojo, opt);
		return stmt;
	}

}
