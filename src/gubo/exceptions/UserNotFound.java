package gubo.exceptions;

public class UserNotFound extends ApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -787639747146671703L;
	final String username;
	public UserNotFound(String username) {
		this.username = username;
	}
	
	public String getMessage() {
		return "User not found: " + this.username;
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