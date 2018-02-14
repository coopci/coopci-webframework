package gubo.exceptions;

public class ApiException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7414527814934184368L;

	public int getHttpStatus() {
		return 200;
	}
	
	public int getCode() {
		return 200;
	}
}
