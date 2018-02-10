package gubo.exceptions;

public class RuntimeExceptionWithCustomContext extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -958518219483633853L;
	
	String customContext="";

	public String getCustomContext() {
		return customContext;
	}

	public void setCustomContext(String customContext) {
		this.customContext = customContext;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
