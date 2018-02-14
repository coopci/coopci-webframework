package gubo.exceptions;

public class LoginAuthFailedException extends ApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3390154336447375956L;
	public LoginAuthFailedException() {
		
	}
	
	public String getMessage() {
		return "用户名或密码错误";
	}
	@Override
	public int getCode() {
		return 401;
	}
	
	@Override
	public int getHttpStatus() {
		return 401;
	}
}
