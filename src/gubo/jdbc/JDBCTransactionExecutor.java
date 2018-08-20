package gubo.jdbc;

import java.sql.Connection;

import javax.sql.DataSource;

/**
 * 	Client code should implement doInTransaction and call execute.
 *	 
 **/
abstract public class JDBCTransactionExecutor<T> {
	
	public T execute(DataSource ds) throws Exception {
		Connection conn = ds.getConnection();
		try {
			if (conn.getAutoCommit()) {
				conn.setAutoCommit(false);
			}
			T ret = this.doInTransaction(conn);
			conn.commit();
			return ret;
		} catch (Exception ex) { 
			conn.rollback();
			throw ex;
		} finally {
			conn.close();
		}
	}
	
	abstract public T doInTransaction(Connection conn) throws Exception;
}
