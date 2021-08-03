package springless.exceptions;

public class PasswordNotMatch extends ApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8666317290850811361L;

	@Override
	public String getMessage() {
		return "PasswordNotMatch";
	}
	@Override
	public int getHttpStatus() {
		return 401;
	}

	@Override
	public int getCode() {
		return 401;
	}
}