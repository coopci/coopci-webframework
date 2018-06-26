package gubo.db;

import gubo.jdbc.mapping.InsertStatementGenerator;
import gubo.jdbc.mapping.ResultSetMapper;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class SimplePoJoDAO {
	
	final private Class<?> clazz;
	public SimplePoJoDAO(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public <T> T loadPoJo(DataSource ds,
			String sql, Object... params) throws SQLException {
		
		Connection dbconn = ds.getConnection();
		try {
			ResultSetMapper<T> mapper = new ResultSetMapper<T>();
			T pojo = mapper.loadPojo(dbconn, this.clazz, sql, params);
			return pojo;	
		} finally {
			dbconn.close();
		}
	}
	
	public <T extends ISimplePoJo> T insert(DataSource ds, T pojo) throws Exception {
		Connection dbconn = ds.getConnection();
		try {
			dbconn.setAutoCommit(false);
			Long newid = InsertStatementGenerator.insertNew(dbconn, pojo);
			pojo.setId(newid);
			return pojo;
		} finally {
			dbconn.close();
		}
	}
}
