package gubo.http.grizzly.demo;

import gubo.http.grizzly.handlers.InMemoryMultipartEntryHandler;

import java.io.IOException;
import java.io.Writer;

import org.glassfish.grizzly.EmptyCompletionHandler;
import org.glassfish.grizzly.http.multipart.MultipartScanner;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

public class MultipartHandler extends HttpHandler {

	/**
	 * Service HTTP request.
	 */
	@Override
	public void service(final Request request, final Response response)
			throws Exception {
		response.suspend();
		final InMemoryMultipartEntryHandler inMemoryMultipartEntryHandler = new InMemoryMultipartEntryHandler();
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
						} catch (IOException ignored) {
						}

						// Resume the asychronous HTTP request processing
						// (in other words finish the asynchronous HTTP request
						// processing).
						response.resume();
					}

					@Override
					public void failed(Throwable throwable) {
						response.resume();
					}

				});
	}
}
