package gubo.db;

import gubo.jdbc.mapping.InsertStatementGenerator;
import gubo.jdbc.mapping.ResultSetMapper;
import gubo.jdbc.mapping.UpdateStatementGenerator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.sql.DataSource;

public class SimplePoJoDAO {

	final private Class<?> clazz;
	final private String tablename;
	
	public SimplePoJoDAO(Class<?> clazz) {
		this.clazz = clazz;
		
		while(true) {
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
	
	public <T> T loadPoJoByPK(DataSource ds, String pkName, Object pkValue)
			throws SQLException {

		Connection dbconn = ds.getConnection();
		try {
			dbconn.setAutoCommit(true);
			ResultSetMapper<T> mapper = new ResultSetMapper<T>();
			String sql = "select * from " + this.tablename + " where " + pkName + " = ?";
			T pojo = mapper.loadPojo(dbconn, this.clazz, sql, pkValue);
			return pojo;
		} finally {
			dbconn.close();
		}
	}
	
	public <T> T loadPoJoByUniqueKey(DataSource ds, String keyName, Object pkValue)
			throws SQLException {

		Connection dbconn = ds.getConnection();
		try {
			dbconn.setAutoCommit(true);
			ResultSetMapper<T> mapper = new ResultSetMapper<T>();
			String sql = "select * from " + this.tablename + " where " + keyName + " = ?";
			T pojo = mapper.loadPojo(dbconn, this.clazz, sql, pkValue);
			return pojo;
		} finally {
			dbconn.close();
		}
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

	public <T extends ISimplePoJo> T update(DataSource ds, T pojo,
			Set<String> allowedCols) throws Exception {
		Connection dbconn = ds.getConnection();
		try {
			dbconn.setAutoCommit(true);
			UpdateStatementGenerator.update(dbconn, pojo, allowedCols);
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

	public <T extends ISimplePoJo> T update(Connection dbconn, T pojo,
			Set<String> allowedCols) throws Exception {
		
		UpdateStatementGenerator.update(dbconn, pojo, allowedCols);
		return pojo;
	}
	
	
}
