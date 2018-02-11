package gubo.http.grizzly;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import gubo.db.ISimplePoJo;
import gubo.exceptions.RequiredParameterException;
import gubo.exceptions.SessionNotFoundException;
import gubo.http.querystring.QueryStringBinder;
import gubo.jdbc.mapping.InsertStatementGenerator;

public class ApiHttpHandler extends NannyHttpHandler {

	// subclass "can" override this method to custom serialization.
	public void sendContent(Object o, Request req, Response response) throws IOException {
		this.sendJson(o, req, response);
	}

	public void serveHead(Request req, Response res) throws Exception {
		Object o = this.doHead(req, res);
		this.sendContent(o, req, res);
	}

	public void serveGet(Request req, Response res) throws Exception {
		Object o = this.doGet(req, res);
		this.sendContent(o, req, res);
	}

	public void servePost(Request req, Response res) throws Exception {
		Object o = this.doPost(req, res);
		this.sendContent(o, req, res);
	}

	public void servePut(Request req, Response res) throws Exception {
		Object o = this.doPut(req, res);
		this.sendContent(o, req, res);
	}

	public void serveDelete(Request req, Response res) throws Exception {
		Object o = this.doDelete(req, res);
		this.sendContent(o, req, res);
	}

	// subclasses should implement these doXXX methods. The returned Object is
	// serialized by this class and the status code will always be 200 upon
	// successful doXXX.
	public Object doHead(Request req, Response res) throws Exception {
		return null;
	}

	public Object doGet(Request req, Response res) throws Exception {
		return null;
	}

	public Object doPost(Request req, Response res) throws Exception {
		return null;
	}

	public Object doPut(Request req, Response res) throws Exception {
		return null;
	}

	public Object doDelete(Request req, Response res) throws Exception {
		return null;
	}

	public HashMap<String, Object> getOKResponse() {
		HashMap<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", "200");
		ret.put("message", "OK");
		return ret;
	}

	public HashMap<String, Object> getErrorResponse(String error, String message) {
		HashMap<String, Object> ret = new HashMap<String, Object>();
		ret.put("code", error);
		ret.put("message", message);
		return ret;
	}

	public void handleException(Exception ex, Request req, Response res) throws Exception {
		if (ex instanceof MySQLIntegrityConstraintViolationException) {
			this.handleException((MySQLIntegrityConstraintViolationException) ex, req, res);
		} else if (ex instanceof SessionNotFoundException ) {
			this.handleException((SessionNotFoundException) ex, req, res);
		} else {
			super.handleException(ex, req, res);
		}
		return;
	}

	public void handleException(MySQLIntegrityConstraintViolationException ex, Request req, Response res)
			throws Exception {
		if (ex.getSQLState().equals("23000")) {
			String msg = ex.getMessage();
			HashMap<String, Object> ret = getErrorResponse("500", msg);
			ret.put("handler", ApiHttpHandler.class);
			this.sendContent(ret, req, res);
			return;
		}
		throw ex;
	}
	
	public void handleException(SessionNotFoundException ex, Request req, Response res)
			throws Exception {
		
		String msg = ex.getMessage();
		HashMap<String, Object> ret = getErrorResponse("500", msg);
		ret.put("message", "SessionNotFound");
		ret.put("sess_id", ex.getSessid());
		ret.put("handler", ApiHttpHandler.class);
		this.sendContent(ret, req, res);
		return;
	}
	

	public HashMap<String, Object> createSimplePojo(Request request, Class<? extends ISimplePoJo> clazz) throws Exception {
		
		ISimplePoJo newPojo = clazz.newInstance();
		
		QueryStringBinder binder = new QueryStringBinder();
		binder.bind(request, newPojo);
		
		Connection dbconn = this.getConnection();
		try {
			dbconn.setAutoCommit(false);
			
			
			InsertStatementGenerator g = new InsertStatementGenerator();
			Long newid = g.insertNew(dbconn, newPojo);
			newPojo.setId(newid);
			dbconn.commit();
			
			HashMap<String, Object> ret = this.getOKResponse();
			ret.put("data", newPojo);
			return ret;

		} catch (Exception ex) {
			dbconn.rollback();
			throw ex;
		} finally {
			dbconn.close();
		}
	}


	public boolean needLogin = true;
	public void checkPermission(Long uid) {
		
	}
	public Long authCheck(Request request) throws NoSuchAlgorithmException, RequiredParameterException, SQLException, SessionNotFoundException {
		if (this.needLogin) {
			Long uid = this.requireLogin(request);
			this.checkPermission(uid);
			return uid;
		}
		return null;
	}
}
