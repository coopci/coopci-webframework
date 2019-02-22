package gubo.http.grizzly.demo;

import gubo.http.grizzly.NannyHttpHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

public class Main {

	public static void main(String[] args) {
		HttpServer server = HttpServer.createSimpleServer("0.0.0.0", 8777);
		server.getServerConfiguration().addHttpHandler(new HttpHandler() {
			@Override
			public void service(Request request, Response response)
					throws Exception {
				final SimpleDateFormat format = new SimpleDateFormat(
						"EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
				final String date = format.format(new Date(System
						.currentTimeMillis()));
				response.setContentType("text/plain");
				response.setContentLength(date.length());
				response.getWriter().write(date);
			}
		}, "/time");

		server.getServerConfiguration().addHttpHandler(new NannyHttpHandler() {
			@Override
			public Object doGet(Request request, Response response)
					throws Exception {
				final SimpleDateFormat format = new SimpleDateFormat(
						"EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
				final String date = format.format(new Date(System
						.currentTimeMillis()));
				return date;
			}
		}, "/time-nanny-serve");

		// curl -XPOST -d'a=1&b=11' 'http://localhost:8777/add'
		server.getServerConfiguration()
				.addHttpHandler(new AddHandler(), "/add");
		// http://localhost:8777/multipart
		server.getServerConfiguration().addHttpHandler(
		        // new MultipartHandler(),
		        new DemoInMemoryMultipartHttpHandler(),
				"/multipart");

		// curl -XPOST -d'a=1&b=11' 'http://localhost:8777/jtwig-html'
		server.getServerConfiguration().addHttpHandler(new HtmlHandler(),
				"/jtwig-html");

		try {

			server.start();
			System.out.println("Press any key to stop the server...");
			System.in.read();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
