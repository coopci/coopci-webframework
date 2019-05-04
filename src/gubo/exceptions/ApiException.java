package gubo.exceptions;

public class ApiException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7414527814934184368L;

	public ApiException(String msg) {
		super(msg);
	}
	public ApiException() {
		
	}
	public int getHttpStatus() {
		return 200;
	}
	
	public int getCode() {
		return 200;
	}
}
