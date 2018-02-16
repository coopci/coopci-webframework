package gubo.exceptions;

import java.util.HashSet;

public class RequiredParametersMissingException extends ApiException {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1674140977712518163L;
	
	HashSet<String> missingParamters;
	public HashSet<String> getMissingParamters() {
		return missingParamters;
	}

	public RequiredParametersMissingException(HashSet<String> missingParamters){
		this.missingParamters = missingParamters;
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
