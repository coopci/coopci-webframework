package gubo.session;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;

import gubo.jdbc.mapping.ResultSetMapper;

/**
 * 用来表示 刻度是 类别的 chart的一个项目，例如 交易分布 交易分布 以 交易品种 和 交易时段 划分交易订单数.
 * 
 */
@Entity(name="session_user")
public class SessionUser {

	@Column(name = "session_id")
	public String session_id;

	@Column(name = "user_id")
	public long user_id;

	@Column(name = "itime")
	public Timestamp itime;

	public static void insert(Connection dbconn, String sessionid, long userid)
			throws NoSuchAlgorithmException, SQLException {

		String sql = "insert into session_user( session_id, user_id)\r\n" + "values (?, ?); ";

		PreparedStatement pstmt = dbconn.prepareStatement(sql);
		pstmt.setString(1, sessionid);
		pstmt.setLong(2, userid);
		pstmt.execute();

	}

	public static void delete(Connection dbconn, String sessionid) throws NoSuchAlgorithmException, SQLException {
		if (sessionid == null)
			return;
		String sql = "delete from session_user \r\n" + "where session_id = ?; ";

		PreparedStatement pstmt = dbconn.prepareStatement(sql);
		pstmt.setString(1, sessionid);
		pstmt.execute();
		return;

	}

	public static SessionUser loadBySessionid(Connection dbconn, String sessid) throws SQLException {
		ResultSetMapper<SessionUser> mapper = new ResultSetMapper<SessionUser>();
		SessionUser su = mapper.loadPojo(dbconn, 
				SessionUser.class, 
				"select * from session_user where session_id=?", 
				sessid);
		return su;
	}

}