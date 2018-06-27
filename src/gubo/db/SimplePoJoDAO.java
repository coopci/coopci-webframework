package gubo.db;

import gubo.jdbc.mapping.InsertStatementGenerator;
import gubo.jdbc.mapping.ResultSetMapper;
import gubo.jdbc.mapping.UpdateStatementGenerator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import javax.sql.DataSource;

public class SimplePoJoDAO {

	final private Class<?> clazz;

	public SimplePoJoDAO(Class<?> clazz) {
		this.clazz = clazz;
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
}
