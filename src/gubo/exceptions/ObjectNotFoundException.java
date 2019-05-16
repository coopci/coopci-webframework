package gubo.exceptions;

public class ObjectNotFoundException extends ApiException {

	private static final long serialVersionUID = 7087901917430232985L;
	protected String message;

	public ObjectNotFoundException(String message) {
		super();
		this.message = "Object not found: " + message;
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
		return 400;
	}
}
