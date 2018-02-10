package gubo.exceptions;

public class SessionNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8871761244756175178L;

	final String sessid;
	public SessionNotFoundException(String s) {
		this.sessid = s;
	}
	public String getSessid() {
		return sessid;
	}

}
