package gubo.exceptions;

public class RequiredParameterException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4286383670561065199L;
	public RequiredParameterException(String pn) {
		super();
		this.parameterName = pn;
	}
	public String parameterName;
	public String getMessage() {
		return "Parameter missing or in wrong format: " + parameterName;
	}
	
	public String getCode() {
		return "400";
	}
}
