package gubo.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class JdbcTemplate {

	public ResultSet query(Connection dbconn, String sql, Object... params)
			throws SQLException {
		PreparedStatement ps = dbconn.prepareStatement(sql);
		int i = 1;
		for (Object o : params) {
			ps.setObject(i++, o);
		}
		ResultSet rs = ps.executeQuery();
		return rs;
	}

	public List<Object[]> queryForRows(Connection dbconn, String sql,
			Object... params) throws SQLException {
		ResultSet rs = query(dbconn, sql, params);
		int columnCount = rs.getMetaData().getColumnCount();
		List<Object[]> ret = new LinkedList<Object[]>();
		while (rs.next()) {
			Object[] row = new Object[columnCount];
			for (int i = 0; i < columnCount; ++i) {
				row[i] = rs.getObject(i + 1);
			}
			ret.add(row);
		}
		rs.close();
		return ret;
	}
	
	public Object queryForObject(Connection dbconn, String sql,
            Object... params) throws SQLException {
        ResultSet rs = query(dbconn, sql, params);
        int columnCount = rs.getMetaData().getColumnCount();
        if (columnCount < 1) {
            return null;
        }
        Object ret = null;
        if (rs.next()) {
            ret = rs.getObject(1);
        }
        rs.close();
        return ret;
    }
	
	public Long queryForLong(Connection dbconn, String sql,
            Object... params) throws SQLException {
        ResultSet rs = query(dbconn, sql, params);
        int columnCount = rs.getMetaData().getColumnCount();
        if (columnCount < 1) {
            return null;
        }
        Long ret = null;
        if (rs.next()) {
            ret = rs.getLong(1);
        }
        rs.close();
        return ret;
    }

	public Object[] queryForFirstRow(Connection dbconn, String sql,
			Object... params) throws SQLException {
		ResultSet rs = query(dbconn, sql, params);
		int columnCount = rs.getMetaData().getColumnCount();

		if (rs.next()) {
			Object[] row = new Object[columnCount];
			for (int i = 0; i < columnCount; ++i) {
				row[i] = rs.getObject(i + 1);
			}
			return row;
		}
		return null;
	}

	public boolean execute(Connection dbconn, String sql, Object... params)
			throws SQLException {
		PreparedStatement ps = dbconn.prepareStatement(sql);
		int i = 1;
		for (Object o : params) {
			ps.setObject(i++, o);
		}
		boolean rs = ps.execute();
		return rs;
	}
	
	public int executeUpdate(Connection dbconn, String sql, Object... params)
			throws SQLException {
		PreparedStatement ps = dbconn.prepareStatement(sql);
		int i = 1;
		for (Object o : params) {
			ps.setObject(i++, o);
		}
		int rowCount = ps.executeUpdate();
		return rowCount;
	}

}
