package gubo.exceptions;

public class PermissionDeniedException extends ApiException {

	private static final long serialVersionUID = 7087901917430232985L;
	protected String message;

	public PermissionDeniedException(String message) {
		super();
		this.message = message;
	}
	public String getMessage() {
		return this.message;
	}
	@Override
	public int getHttpStatus() {
		return 200;
	}
	
	@Override
	public int getCode() {
		return 403;
	}
}
