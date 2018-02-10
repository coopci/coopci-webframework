package gubo.http.grizzly;

import java.io.IOException;
import java.util.HashMap;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

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
}
