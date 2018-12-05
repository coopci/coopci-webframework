package gubo.http.grizzly.handlers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.glassfish.grizzly.ReadHandler;
import org.glassfish.grizzly.http.io.NIOInputStream;
import org.glassfish.grizzly.http.multipart.ContentDisposition;
import org.glassfish.grizzly.http.multipart.MultipartEntry;
import org.glassfish.grizzly.http.multipart.MultipartEntryHandler;

/**
 * Make https://javaee.github.io/grizzly/httpserverframeworkextras.html#/
 * Multipart_multipartform-data_HTTP_Requests_Processing easier at the cost of
 * performance and flexibility.
 * 
 * See {@link gubo.http.grizzly.demo.MultipartHandler} for usage.
 * 
 **/
public class InMemoryMultipartEntryHandler implements MultipartEntryHandler {

	// TODO different size limit on different multipartEntry
	// TODO make it even easier to use
	class BytesReadHandler implements ReadHandler {
		private final MultipartEntry multipartEntry;

		private final NIOInputStream inputStream;

		private final byte[] buf;
		int offset = 0;

		byte[] data;

		public MultipartEntry getMultipartEntry() {
			return multipartEntry;
		}

		public byte[] getData() {
			return data;
		}

		// private ByteBuffer bb;

		private BytesReadHandler(final MultipartEntry multipartEntry,
				int bufSize) {
			this.multipartEntry = multipartEntry;
			this.inputStream = this.multipartEntry.getNIOInputStream();
			buf = new byte[bufSize];
			// bb = ByteBuffer.allocate(bufSize);
		}

		private BytesReadHandler(final MultipartEntry multipartEntry) {
			this(multipartEntry, 2048);
		}

		@Override
		public void onDataAvailable() throws Exception {
			readAndSaveAvail();
		}

		@Override
		public void onError(Throwable t) {
		}

		@Override
		public void onAllDataRead() throws Exception {
			readAndSaveAvail();
			this.data = Arrays.copyOfRange(this.buf, 0, offset);
		}

		private void readAndSaveAvail() throws IOException {

			while (inputStream.isReady()) {

				if (buf.length <= offset + 1) {
					inputStream.close();
					throw new IllegalArgumentException("parameter "
							+ this.multipartEntry.getContentDisposition()
									.getDispositionParam("name")
							+ " is too long, size limit is " + this.buf.length);
					// break;
				}
				final int readBytes = inputStream.read(buf, offset, buf.length
						- offset - 1);
				offset += readBytes;

			}
		}
	}

	public ConcurrentHashMap<String, BytesReadHandler> multipartEntries = new ConcurrentHashMap<String, BytesReadHandler>();

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, BytesReadHandler> entry : multipartEntries
				.entrySet()) {
			sb.append(entry.getKey());
			sb.append(": ");
			sb.append(entry.getValue().getData().length);
			sb.append("\n");
		}
		return sb.toString();
	}

	@Override
	public void handle(MultipartEntry multipartEntry) throws Exception {
		final ContentDisposition contentDisposition = multipartEntry
				.getContentDisposition();
		final String name = contentDisposition
				.getDispositionParamUnquoted("name");
		BytesReadHandler brh = new BytesReadHandler(multipartEntry);

		this.multipartEntries.put(name, brh);
		final NIOInputStream inputStream = multipartEntry.getNIOInputStream();

		inputStream.notifyAvailable(brh);
	}

}
