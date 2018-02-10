package gubo.exceptions;

public class SessionNotFound extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8871761244756175178L;

	final String sessid;
	public SessionNotFound(String s) {
		this.sessid = s;
	}
	public String getSessid() {
		return sessid;
	}

}
