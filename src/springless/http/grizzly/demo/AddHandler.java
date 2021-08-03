package springless.http.grizzly.demo;

import springless.http.grizzly.NannyHttpHandler;
import springless.http.querystring.QueryStringField;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

/**
 * Demo to convert POST data to pojo with QueryString
 * 
 * curl -XPOST -d'a=1&b=11' 'http://localhost:8777/add'
 **/
public class AddHandler extends NannyHttpHandler {

	public class AddHandlerParameter {
		@QueryStringField(name = "a")
		public int oprand1;

		@QueryStringField()
		public int b;

		int doAdd() {
			return this.oprand1 + this.b;

		}
	}

	@Override
	public Object doPost(Request request, Response response) throws Exception {
		AddHandlerParameter p = new AddHandlerParameter();
		this.bindParameter(request, p);
		return p.doAdd();
	}
}
