package gubo.exceptions;

public class BadParameterException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 428638368019065199L;
	protected String message;

	public BadParameterException(String message) {
		super();
		this.message = message;
	}
	public String getMessage() {
		return this.message;
	}
	
	public String getCode() {
		return "400";
	}
}
