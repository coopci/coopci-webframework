package gubo.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.sql.DataSource;

import gubo.jdbc.mapping.InsertStatementGenerator;
import gubo.jdbc.mapping.ResultSetMapper;
import gubo.jdbc.mapping.UpdateStatementGenerator;

public class SimplePoJoDAO {

	final private Class<?> clazz;
	final private String tablename;

	public SimplePoJoDAO(Class<?> clazz) {
		this.clazz = clazz;

		while (true) {
			Table table = this.clazz.getAnnotation(Table.class);
			if (table != null) {
				this.tablename = table.name();
				break;
			}
			Entity entity = this.clazz.getAnnotation(Entity.class);
			if (entity != null) {
				this.tablename = entity.name();
				break;
			}
			this.tablename = null;
			break;
		}

	}

	public <T> T loadPoJo(DataSource ds, String sql, Object... params)
			throws SQLException {

		Connection dbconn = ds.getConnection();
		try {
			dbconn.setAutoCommit(true);
			ResultSetMapper<T> mapper = new ResultSetMapper<T>();
			T pojo = mapper.loadPojo(dbconn, this.clazz, sql, params);
			return pojo;
		} finally {
			dbconn.close();
		}
	}

	public <T> List<T> loadPojoList(Connection dbconn, String sql,
			Object... params) throws SQLException {

		ResultSetMapper<T> mapper = new ResultSetMapper<T>();
		List<T> pojoList = mapper.loadPojoList(dbconn, this.clazz, sql, params);
		return pojoList;
	}
	
	public <T> T loadPojo(Connection dbconn, String sql,
			Object... params) throws SQLException {

		List<T>  l = loadPojoList( dbconn,  sql, params);
		if (l.isEmpty()) {
			return null;
		} else {
			return l.get(0);
		}
	}
	
	public <T> T loadPojo(DataSource ds, String sql,
			Object... params) throws SQLException {
		try(Connection dbconn = ds.getConnection()) {
			return loadPojo(dbconn, sql, params);
		}
	}

	public <T> List<T> loadPojoList(DataSource ds, String sql, Object... params)
			throws SQLException {
		Connection dbconn = ds.getConnection();
		try {
			dbconn.setAutoCommit(true);
			return this.loadPojoList(dbconn, sql, params);
		} finally {
			dbconn.close();
		}
	}

	public <T> T loadPoJoByPK(DataSource ds, String pkName, Object pkValue)
			throws SQLException {

		Connection dbconn = ds.getConnection();
		try {
			dbconn.setAutoCommit(true);
			return this.loadPoJoByPK(dbconn,  pkName,  pkValue);
		} finally {
			dbconn.close();
		}
	}
	public <T> T loadPoJoByPK(Connection dbconn, String pkName, Object pkValue)
			throws SQLException {

		ResultSetMapper<T> mapper = new ResultSetMapper<T>();
		String sql = "select * from " + this.tablename + " where " + pkName
				+ " = ?";
		T pojo = mapper.loadPojo(dbconn, this.clazz, sql, pkValue);
		return pojo;
		
	}
	public <T> T loadPoJoByUniqueKey(DataSource ds, String keyName,
			Object pkValue) throws SQLException {

		Connection dbconn = ds.getConnection();
		try {
			dbconn.setAutoCommit(true);
			ResultSetMapper<T> mapper = new ResultSetMapper<T>();
			String sql = "select * from " + this.tablename + " where "
					+ keyName + " = ?";
			T pojo = mapper.loadPojo(dbconn, this.clazz, sql, pkValue);
			return pojo;
		} finally {
			dbconn.close();
		}
	}
	
	public <T> T loadPoJoByUniqueKey(Connection dbconn, String keyName,
			Object pkValue) throws SQLException {
		ResultSetMapper<T> mapper = new ResultSetMapper<T>();
		String sql = "select * from " + this.tablename + " where "
				+ keyName + " = ?";
		T pojo = mapper.loadPojo(dbconn, this.clazz, sql, pkValue);
		return pojo;
	}
	
	/**
	 * @param keyNamesAndvalues里面 偶数下标的是列名，奇数下标的是值。
	 **/
	public <T> T loadPoJoByUniqueKeys(Connection dbconn,
			Object ... keyNamesAndvalues) throws SQLException {
		ResultSetMapper<T> mapper = new ResultSetMapper<T>();
		
		LinkedList<String> names = new LinkedList<String>();
		LinkedList<Object> values = new LinkedList<Object>();
		int i = 0;
		for (Object nOrV : keyNamesAndvalues) {
			if (i % 2 ==0 ) {
				// nOrV 是列名
				names.add((String)nOrV);
			} else {
				// nOrV 是值
				values.add(nOrV);
			}
			i++;
		}
		// String sql = "select * from " + this.tablename + " where "
//				+ keyName + " = ?";
		StringBuilder sql = new StringBuilder("select * from " + this.tablename + " where 1=1 ");
		for (String name : names) {
			sql.append(" AND `" + name + "` = ?");
		}
		System.out.println(sql.toString());
		List<T> pojoList = mapper.loadPojoList(dbconn, this.clazz, sql.toString(), values.toArray());
		if (pojoList.isEmpty()) {
			return null;
		}
		if (pojoList.size() > 1) {
			throw new RuntimeException("Found " + pojoList.size() + " rows, while there should only be one row.");
			
		}
		return pojoList.get(0);
	}

	/**
	 * id字段由mysql autoinrement，这里把生成的id赋值 给pojo.id之后才返回。 
	 **/
	public <T extends ISimplePoJo> T insert(Connection dbconn, T pojo)
			throws Exception {
		Long newid = InsertStatementGenerator.insertNew(dbconn, pojo);
		pojo.setId(newid);
		return pojo;
	}

	public <T extends ISimplePoJo> T insert(Connection dbconn, T pojo,
			boolean autoId) throws Exception {
		Long newid = InsertStatementGenerator.insertNew(dbconn, pojo);
		if (autoId) {
			pojo.setId(newid);
		}
		return pojo;
	}

	public <T extends ISimplePoJo> T insert(DataSource ds, T pojo)
			throws Exception {
		Connection dbconn = ds.getConnection();
		try {
			dbconn.setAutoCommit(true);
			Long newid = InsertStatementGenerator.insertNew(dbconn, pojo);
			pojo.setId(newid);
			return pojo;
		} finally {
			dbconn.close();
		}
	}

	public <T extends ISimplePoJo> T update(DataSource ds, T pojo)
			throws Exception {
		Connection dbconn = ds.getConnection();
		try {
			dbconn.setAutoCommit(true);
			UpdateStatementGenerator.update(dbconn, pojo);
			return pojo;
		} finally {
			dbconn.close();
		}
	}

	public <T extends ISimplePoJo> T update(Connection dbconn, T pojo)
			throws Exception {
		UpdateStatementGenerator.update(dbconn, pojo);
		return pojo;
	}

	public <T extends ISimplePoJo> T update(DataSource ds, T pojo,
			String... allowedCols) throws Exception {

		HashSet<String> cols = new HashSet<String>();
		for (String col : allowedCols) {
			cols.add(col);
		}

		Connection dbconn = ds.getConnection();
		try {
			dbconn.setAutoCommit(true);
			UpdateStatementGenerator.update(dbconn, pojo, cols);
			return pojo;
		} finally {
			dbconn.close();
		}
	}

	public <T extends ISimplePoJo> T update(Connection dbconn, T pojo,
			String... allowedCols) throws Exception {

		HashSet<String> cols = new HashSet<String>();
		for (String col : allowedCols) {
			cols.add(col);
		}
		UpdateStatementGenerator.update(dbconn, pojo, cols);
		return pojo;
	}

}
