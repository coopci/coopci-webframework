package gubo.exceptions;

public class QueryStringParseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2369250427614357836L;

	public String getFieldname() {
		return fieldname;
	}
	public String getRawValue() {
		return rawValue;
	}
	final private String fieldname;
	final private String rawValue;
	public QueryStringParseException(String fn, String rawValue, Throwable cause) {
		super("QueryStringParseException, fieldname: " + fn + ", rawValue: " + rawValue, cause);
		this.fieldname = fn;
		this.rawValue = rawValue;
		
	}
	
}
