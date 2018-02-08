package gubo.exceptions;

public class LoginAuthFailedException extends Exception {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3390154336447375956L;
	public LoginAuthFailedException() {
		
	}
	
	public String getMessage() {
		return "用户名或密码错误";
	}
	public String getCode() {
		return "401";
	}
}
