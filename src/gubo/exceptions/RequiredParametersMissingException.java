package gubo.exceptions;

import java.util.HashSet;

public class RequiredParametersMissingException extends ApiException {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1674140977712518163L;
	
	HashSet<String> missingParamters;
	 Class<?> clazz;
	public HashSet<String> getMissingParamters() {
		return missingParamters;
	}

	public RequiredParametersMissingException(HashSet<String> missingParamters,  Class<?> clazz){
		this.missingParamters = missingParamters;
		this.clazz = clazz;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nThese required parameters are missing: ");
		boolean isFirst = true;
		for (String p : missingParamters) {
			if (!isFirst)
				sb.append(", ");
			sb.append(p);
			isFirst = false;
		}
		sb.append("for instance of class")
		.append(this.clazz.getName());
		String ret = sb.toString();
		return ret;
	}

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nThese required parameters are missing: ");
		boolean isFirst = true;
		for (String p : missingParamters) {
			if (!isFirst)
				sb.append(", ");
			sb.append(p);
			isFirst = false;
		}
		String ret = sb.toString();
		return ret;
	}

	public int getHttpStatus() {
		return 400;
	}
	
	public int getCode() {
		return 400;
	}
}
