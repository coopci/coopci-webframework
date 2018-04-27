package gubo.http.grizzly.handlers;

import gubo.http.grizzly.NannyHttpHandler;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

public class TestStringHandler extends NannyHttpHandler {
	@Override
	public String doGet(Request request, Response response)
			throws Exception {
		return "hello world.";
	}
}