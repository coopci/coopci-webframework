package springless.exceptions;

public class BadParameterException extends ApiException {

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
	@Override
	public int getCode() {
		return 400;
	}
	
	@Override
	public int getHttpStatus() {
		return 200;
	}
}
