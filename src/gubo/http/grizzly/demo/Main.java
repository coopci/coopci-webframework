package gubo.http.grizzly.demo;

import gubo.http.grizzly.NannyHttpHandler;
import gubo.http.grizzly.handlers.InMemoryMultipartEntryHandler;
import gubo.http.grizzly.handlers.mapping.service.ServiceRegistry;
import gubo.http.querystring.QueryStringBinder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.glassfish.grizzly.http.multipart.ContentDisposition;
import org.glassfish.grizzly.http.multipart.MultipartEntry;
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

		server.getServerConfiguration().addHttpHandler(new HttpHandler() {
			@Override
			public void service(Request request, Response response)
					throws Exception {
				
				response.sendRedirect("http://non-exist.com/a.html");
			}
		}, "/bdk");
		
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
		
		server.getServerConfiguration().addHttpHandler(
		        // new MultipartHandler(),
		        new NannyHttpHandler() {
		        	@Override
		        	public int getDefaultSizeLimit() {
		        		// return InMemoryMultipartEntryHandler.SIZE_LIMIT_IGNORE;
		        		return 10000;
		        	}
		        	@Override
		        	public Object doPost(final Request request, final Response response,
		        			final InMemoryMultipartEntryHandler inMemoryMultipartEntryHandler) throws Exception {
		        		
		        		DemoRequest p = new DemoRequest();
		        		final QueryStringBinder binder = new QueryStringBinder();
		        		binder.bind(inMemoryMultipartEntryHandler, p); // 这样可以把所有的数据都放到p里，包括 multipart的文件。
		        		String s = new String(p.qualification.getBytes());
		        		// 下面的代码只是用来演示 inMemoryMultipartEntryHandler。
		        		// 大部分程序 完全可以只用 p，而不需要写下面这样的代码。
		        		
		                String s1 = inMemoryMultipartEntryHandler
		                        .getString("sfsd");
		                String s2 = inMemoryMultipartEntryHandler
		                        .getString("fff");
		                
		                @SuppressWarnings("unused")
		                MultipartEntry multipartEntry = inMemoryMultipartEntryHandler.getMultipartEntry("fff");
		                ContentDisposition contentDisposition = inMemoryMultipartEntryHandler.getContentDisposition("fff");
		                
		                String filename = contentDisposition.getDispositionParam("filename");
		                

		                Map<String, String> params = inMemoryMultipartEntryHandler.getMap();
		                System.out.println("filename: " + filename);
		                System.out.println("filecontent: " + s2);
		                
		        		return params;
		        	}
		        },
				"/multipart-nanny");

		// curl -XPOST -d'a=1&b=11' 'http://localhost:8777/jtwig-html'
		server.getServerConfiguration().addHttpHandler(new HtmlHandler(),
				"/jtwig-html");

		ServiceRegistry.register(new DemoService(), server, "");
		try {

			server.start();
			System.out.println("Press any key to stop the server...");
			System.in.read();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
