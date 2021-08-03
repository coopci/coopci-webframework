package springless.jsonrpc;

public class JsonRpcResult<T> {

	private T result;
	private String rawResult;
	
	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public String getRawResult() {
		return rawResult;
	}

	public void setRawResult(String rawResult) {
		this.rawResult = rawResult;
	}
	public JsonRpcResult (T r, String raw) {
		this.result = r;
		this.rawResult = raw;
	}
}
