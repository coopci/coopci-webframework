package gubo.http.grizzly.handlers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;
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

	/**
	 * Ignore this multipart entry regardless its size.
	 **/
	public final static int SIZE_LIMIT_IGNORE = -2;

	/**
	 * Ignore this disposition regardless its size.
	 **/
	public final static int SIZE_LIMIT_REJECT = -3;

	public InMemoryMultipartEntryHandler() {
		this(null, 2048);
	}

	int defaulSizeLimit = 2048;
	Map<String, Integer> sizeLimit;

	public InMemoryMultipartEntryHandler(Map<String, Integer> sizeLimit) {
		this(sizeLimit, 2048);
	}

	public InMemoryMultipartEntryHandler(Map<String, Integer> sizeLimit,
			int defaulSizeLimit) {
		this.defaulSizeLimit = defaulSizeLimit;
		this.sizeLimit = sizeLimit;
	}

	class BytesReadHandler implements ReadHandler {
		private final MultipartEntry multipartEntry;

		private final NIOInputStream inputStream;

		private byte[] buf;
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
			this.buf = null;
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

	private final ConcurrentHashMap<String, BytesReadHandler> multipartEntries = new ConcurrentHashMap<String, BytesReadHandler>();

	public ConcurrentHashMap<String, BytesReadHandler> getMultipartEntries() {
		return multipartEntries;
	}

	public MultipartEntry getMultipartEntry(String name) {
		if (!this.multipartEntries.contains(name)) {
			return null;
		}
		return this.multipartEntries.get(name).multipartEntry;
	}

	public ContentDisposition getContentDisposition(String name) {
		if (!this.multipartEntries.contains(name)) {
			return null;
		}
		return this.multipartEntries.get(name).multipartEntry
				.getContentDisposition();
	}

	public byte[] getBytes(String name) {
		if (!this.multipartEntries.containsKey(name)) {
			return null;
		}
		return this.multipartEntries.get(name).data;
	}

	public String getString(String name, String charset)
			throws UnsupportedEncodingException {
		byte[] bytes = this.getBytes(name);
		if (bytes == null) {
			return null;
		}
		String ret = new String(bytes, charset);
		return ret;
	}

	public String getString(String name) throws UnsupportedEncodingException {
		return this.getString(name, "utf-8");
	}

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

	Integer getEffecitveSizeLimit(String name) {
		Integer effecitveSizeLimit = this.defaulSizeLimit;
		if (this.sizeLimit != null && this.sizeLimit.containsKey(name)) {
			effecitveSizeLimit = this.sizeLimit.get(name);
		}
		return effecitveSizeLimit;
	}

	void checkReject(String name) {
		Integer effecitveSizeLimit = this.getEffecitveSizeLimit(name);
		if (SIZE_LIMIT_REJECT == effecitveSizeLimit) {
			throw new IllegalArgumentException("parameter " + name
					+ " is not allowed");
		}
	}

	boolean checkIgnore(String name) {
		Integer effecitveSizeLimit = this.getEffecitveSizeLimit(name);
		if (SIZE_LIMIT_IGNORE == effecitveSizeLimit) {
			return true;
		}
		return false;
	}

	@Override
	public void handle(MultipartEntry multipartEntry) throws Exception {
		final ContentDisposition contentDisposition = multipartEntry
				.getContentDisposition();
		final String name = contentDisposition
				.getDispositionParamUnquoted("name");

		this.checkReject(name);

		boolean ignore = this.checkIgnore(name);
		if (ignore) {
			multipartEntry.skip();
		} else {
			BytesReadHandler brh = new BytesReadHandler(multipartEntry,
					this.getEffecitveSizeLimit(name));

			this.multipartEntries.put(name, brh);
			final NIOInputStream inputStream = multipartEntry
					.getNIOInputStream();

			inputStream.notifyAvailable(brh);
		}
	}

}
