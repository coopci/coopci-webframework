package gubo.http.grizzly;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;


// 用 jtwig 渲染一个页面。
public class JtwigHandler extends NannyHttpHandler {

	public static class ModelAndTemplate {
		private JtwigModel model;
		public JtwigModel getModel() {
			return model;
		}
		public void setModel(JtwigModel model) {
			this.model = model;
		}
		public JtwigTemplate getTemplate() {
			return template;
		}
		public void setTemplate(JtwigTemplate template) {
			this.template = template;
		}
		private JtwigTemplate template;
		
		public ModelAndTemplate(JtwigModel m, JtwigTemplate t) {
			this.model = m;
			this.template = t;
		}
	}
	public void sendContent(ModelAndTemplate mat, Request req, Response response) throws IOException {

		JtwigTemplate template = mat.getTemplate();
		response.setContentType("text/html; charset=UTF-8");
        String content = template.render(mat.getModel());
        response.getWriter().write(content);
	}

	public void serveHead(Request req, Response res) throws Exception {
		this.setCrossDomain(res);
		ModelAndTemplate o = this.doHead(req, res);
		this.sendContent(o, req, res);
	}

	public void serveGet(Request req, Response res) throws Exception {
		this.setCrossDomain(res);
		ModelAndTemplate o = this.doGet(req, res);
		this.sendContent(o, req, res);
	}

	public void servePost(Request req, Response res) throws Exception {
		this.setCrossDomain(res);
		ModelAndTemplate o = this.doPost(req, res);
		this.sendContent(o, req, res);
	}

	public void servePut(Request req, Response res) throws Exception {
		this.setCrossDomain(res);
		ModelAndTemplate o = this.doPut(req, res);
		this.sendContent(o, req, res);
	}
	
	public void setCrossDomain (Response response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		
	}

	public void serveDelete(Request req, Response res) throws Exception {
		this.setCrossDomain(res);
		ModelAndTemplate o = this.doDelete(req, res);
		this.sendContent(o, req, res);
	}

	// subclasses should implement these doXXX methods. The returned Object is
	// serialized by this class and the status code will always be 200 upon
	// successful doXXX.
	public ModelAndTemplate doHead(Request req, Response res) throws Exception {
		return null;
	}

	public ModelAndTemplate doGet(Request req, Response res) throws Exception {
		return null;
	}

	public ModelAndTemplate doPost(Request req, Response res) throws Exception {
		return null;
	}

	public ModelAndTemplate doPut(Request req, Response res) throws Exception {
		return null;
	}

	public ModelAndTemplate doDelete(Request req, Response res) throws Exception {
		return null;
	}

	@Override
	public Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}
