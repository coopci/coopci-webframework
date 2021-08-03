package springless.http.grizzly.demo;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

import org.glassfish.grizzly.EmptyCompletionHandler;
import org.glassfish.grizzly.http.multipart.ContentDisposition;
import org.glassfish.grizzly.http.multipart.MultipartScanner;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import springless.http.grizzly.handlers.InMemoryMultipartEntryHandler;

/**
 * 
 * 演示 用 InMemoryMultipartEntryHandler 处理 form-data。
 **/
public class MultipartHandler extends HttpHandler {

	/**
	 * Service HTTP request.
	 */
	@Override
	public void service(final Request request, final Response response)
			throws Exception {
		response.suspend();
		HashMap<String, Integer> sizeLimit = new HashMap<String, Integer>();
		sizeLimit.put("ignore-this",
				InMemoryMultipartEntryHandler.SIZE_LIMIT_IGNORE);
		sizeLimit.put("reject-this",
				InMemoryMultipartEntryHandler.SIZE_LIMIT_REJECT);
		sizeLimit.put("big-one", 1024 * 1024);
		sizeLimit.put("fff", 1024 * 1024);

		// 拒绝叫做 reject-this 的参数。

		final InMemoryMultipartEntryHandler inMemoryMultipartEntryHandler = new InMemoryMultipartEntryHandler(
				sizeLimit,
				// 忽略 big-one 和 fff 以外的所有参数。
				InMemoryMultipartEntryHandler.SIZE_LIMIT_IGNORE);
		// Start the asynchronous multipart request scanning...
		MultipartScanner.scan(request, inMemoryMultipartEntryHandler,
				new EmptyCompletionHandler<Request>() {
					// CompletionHandler is called once HTTP request processing
					// is completed
					// or failed.
					@Override
					public void completed(final Request request) {

						try {
							// process the data in inMemoryMultipartEntryHandler
							// inMemoryMultipartEntryHandler.multipartEntries
							// getValue.getData() ...

							response.setContentType("text/plain");
							final Writer writer = response.getWriter();
							writer.write(inMemoryMultipartEntryHandler
									.toString());

							String s1 = inMemoryMultipartEntryHandler
									.getString("sfsd");
							String s2 = inMemoryMultipartEntryHandler
									.getString("fff");
							// MultipartEntry multipartEntry = inMemoryMultipartEntryHandler.getMultipartEntry("fff");
							ContentDisposition contentDisposition = inMemoryMultipartEntryHandler.getContentDisposition("fff");
							
							String filename = contentDisposition.getDispositionParam("filename");
							
							String s3 = inMemoryMultipartEntryHandler
									.getString("big-one");

							writer.write(s1 + "\n");
							writer.write(s3 + "\n");
							writer.write("filename: " + filename + "\n");
							writer.write("fileconent: " + s2 + "\n");
						} catch (IOException ignored) {
						}

						// Resume the asychronous HTTP request processing
						// (in other words finish the asynchronous HTTP request
						// processing).
						response.resume();
					}

					@Override
					public void failed(Throwable throwable) {
						final Writer writer = response.getWriter();
						try {
							writer.write(throwable.getMessage());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						response.resume();
					}

				});
	}
}
