package springless.http.grizzly.handlers;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import springless.http.grizzly.NannyHttpHandler;

public class TestStringHandler extends NannyHttpHandler {
	@Override
	public String doGet(Request request, Response response)
			throws Exception {
		return "hello world.";
	}
}