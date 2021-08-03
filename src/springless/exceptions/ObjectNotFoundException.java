package springless.exceptions;

import com.google.common.base.Strings;

public class ObjectNotFoundException extends ApiException {

	private static final long serialVersionUID = 7087901917430232985L;
	protected String message;

	public ObjectNotFoundException(String message) {
        super();
        if (!Strings.isNullOrEmpty(message)) {
            this.message = message;
        } else {
            this.message = "Object not found";
        }
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
