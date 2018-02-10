package gubo.session;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import gubo.exceptions.SessionNotFoundException;

public class SessonManager {
	public static String generateNewSessionId() {
		String sessid = UUID.randomUUID().toString();
		return sessid;
	}
	
	ConcurrentHashMap<String, Long> _cache = new ConcurrentHashMap<String, Long>();
	
	public void add(Connection dbconn, String sess_id, long userid) throws NoSuchAlgorithmException, SQLException {
		
		SessionUser.insert( dbconn,  sess_id,  userid);
		_cache.put(sess_id, userid);
	}
	public void delete(Connection dbconn, String sess_id) throws NoSuchAlgorithmException, SQLException {
		SessionUser.delete( dbconn,  sess_id);
		_cache.remove(sess_id);
	}
	
	public Long get(Connection dbconn, String sess_id, boolean throwOnNotFound) throws NoSuchAlgorithmException, SQLException, SessionNotFoundException {
		Long uid = this._cache.get(sess_id);
		if (uid == null) {
			SessionUser su = SessionUser.loadBySessionid(dbconn, sess_id);
			if (su != null) {
				this._cache.put(sess_id, su.user_id);
				uid = su.user_id;
			}
		}
		
		if (uid == null && throwOnNotFound) {
			throw new SessionNotFoundException(sess_id);
			
		}
		return uid;
	}
	
}
