package gubo.http.grizzly;

import gubo.exceptions.BadParameterException;
import gubo.exceptions.RequiredParameterException;

import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.HttpStatus;

public class NannyHttpHandler extends HttpHandler {

	public void serveHead(Request req, Response res) throws Exception {
		res.setStatus(HttpStatus.METHOD_NOT_ALLOWED_405);
	}

	public void serveGet(Request req, Response res) throws Exception {
		res.setStatus(HttpStatus.METHOD_NOT_ALLOWED_405);
	}

	public void servePost(Request req, Response res) throws Exception {
		res.setStatus(HttpStatus.METHOD_NOT_ALLOWED_405);
	}

	public void servePut(Request req, Response res) throws Exception {
		res.setStatus(HttpStatus.METHOD_NOT_ALLOWED_405);
	}

	public void serveDelete(Request req, Response res) throws Exception {
		res.setStatus(HttpStatus.METHOD_NOT_ALLOWED_405);
	}

	public void handleException(Exception ex) throws Exception {
		throw ex;
	}

	@Override
	public void service(Request req, Response res) throws Exception {

		Method method = req.getMethod();

		try {
			if (method.getMethodString().equals("GET")) {
				this.serveGet(req, res);
			} else if (method.getMethodString().equals("POST")) {
				this.servePost(req, res);
			} else if (method.getMethodString().equals("HEAD")) {
				this.serveHead(req, res);
			} else if (method.getMethodString().equals("PUT")) {
				this.servePut(req, res);
			} else if (method.getMethodString().equals("DELETE")) {
				this.serveDelete(req, res);
			}

		} catch (Exception ex) {
			this.handleException(ex);
		}
	}

	public long getRequiredLongParameter(Request request, String pname)
			throws RequiredParameterException {
		String ret = request.getParameter(pname);
		if (ret == null || ret.length() == 0)
			throw new RequiredParameterException(pname);
		try {
			return Long.parseLong(ret);
		} catch (Exception e) {
			throw new RequiredParameterException(pname);
		}
	}

	public double getRequiredDoubleParameter(Request request, String pname)
			throws RequiredParameterException {
		String ret = request.getParameter(pname);
		if (ret == null || ret.length() == 0)
			throw new RequiredParameterException(pname);
		try {
			return Double.parseDouble(ret);
		} catch (Exception e) {
			throw new RequiredParameterException(pname);
		}
	}

	/**
	 * 获取double类型的参数，如果参数不存在返回defaultValue。如果参数错误，
	 * throwExceptionWhenError为true时抛出异常，
	 * throwExceptionWhenError为false时返回defaultValue。
	 * 
	 * @param request
	 * @param pname
	 * @param defaultValue
	 * @param throwExceptionWhenError
	 * @return
	 * @throws BadParameterException
	 */
	public double getDoubleParameter(Request request, String pname,
			double defaultValue, boolean throwExceptionWhenError)
			throws BadParameterException {
		String ret = request.getParameter(pname);
		if (ret == null || ret.isEmpty()) {
			return defaultValue;
		}
		try {
			return Double.parseDouble(ret);
		} catch (Exception e) {
			if (throwExceptionWhenError) {
				throw new BadParameterException("Bad parameter '" + pname + "'");
			}
		}
		return 0.0;
	}

	public String getRequiredStringParameter(Request request, String pname)
			throws RequiredParameterException {
		String ret = request.getParameter(pname);
		if (ret == null || ret.length() == 0)
			throw new RequiredParameterException(pname);
		return ret;
	}

	/**
	 * 获取string类型的参数值，如果为null或空字符串，则返回缺省值。
	 * 
	 * @param request
	 * @param pname
	 * @param defaultValue
	 * @return
	 */
	public String getStringParameter(Request request, String pname,
			String defaultValue) {
		String ret = request.getParameter(pname);
		if (ret == null || ret.isEmpty()) {
			return defaultValue;
		}
		return ret;
	}

}
