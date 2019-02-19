package gubo.http.grizzly.handlers;

import org.glassfish.grizzly.EmptyCompletionHandler;
import org.glassfish.grizzly.http.multipart.MultipartScanner;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

/**
 * 
 * 用 InMemoryMultipartEntryHandler 处理 form-data。
 **/
abstract public class InMemoryMultipartHttpHandler extends HttpHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    abstract public void onMultipartScanCompleted(final Request request, final Response response, final InMemoryMultipartEntryHandler inMemoryMultipartEntryHandler) throws IOException;
    public HashMap<String, Integer> getSizeLimit() {
        return null;
    }
    public int getDefaultSizeLimit() {
        return InMemoryMultipartEntryHandler.SIZE_LIMIT_IGNORE;
    }
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
		        getSizeLimit(),
				
				getDefaultSizeLimit());
		
		
		// Start the asynchronous multipart request scanning...
		MultipartScanner.scan(request, inMemoryMultipartEntryHandler,
				new EmptyCompletionHandler<Request>() {
					// CompletionHandler is called once HTTP request processing
					// is completed
					// or failed.
					@Override
					public void completed(final Request request) {

						try {
						    onMultipartScanCompleted(request, response, inMemoryMultipartEntryHandler);
						} catch (Exception ex) {
						    logger.error("onMultipartScanCompleted", ex);
						} finally {
	                        // Resume the asychronous HTTP request processing
	                        // (in other words finish the asynchronous HTTP request
	                        // processing).
	                        response.resume();
						}
					}

					@Override
					public void failed(Throwable throwable) {
						final Writer writer = response.getWriter();
						try {
							writer.write(throwable.getMessage());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
						    response.resume();
						}
					}

				});
	}
}
