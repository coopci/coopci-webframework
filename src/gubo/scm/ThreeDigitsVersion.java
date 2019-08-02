package gubo.scm;

import java.util.Objects;

public class ThreeDigitsVersion implements Comparable<ThreeDigitsVersion>{
	private final int digit1;
	private final int digit2;
	private final int digit3;
	
	public ThreeDigitsVersion(String versinoNumber) {
		String[] digits = versinoNumber.split("\\.");
		
		this.digit1 = Integer.parseInt(digits[0]);
		this.digit2 = Integer.parseInt(digits[1]);
		this.digit3 = Integer.parseInt(digits[2]);
	}
	
	@Override
	public int compareTo(ThreeDigitsVersion other) {
		
		if (this.digit1 < other.digit1) {
			return -1;
		} else if (this.digit1 > other.digit1) {
			return 1;
		}
		// digit1 equal
		if (this.digit2 < other.digit2) {
			return -1;
		} else if (this.digit2 > other.digit2) {
			return 1;
		}
		// digit1 equal		
		if (this.digit3 < other.digit3) {
			return -1;
		} else if (this.digit3 > other.digit3) {
			return 1;
		}
		// digit1 equal
		return 0;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (! (other instanceof ThreeDigitsVersion)){
			return false;
		}
		ThreeDigitsVersion ov = (ThreeDigitsVersion) other;
		return this.digit1 == ov.digit1 && 
				this.digit2 == ov.digit2 &&
				this.digit3 == ov.digit3 
				;
	}
	@Override 
	public int hashCode() {
	    return Objects.hash(this.digit1, this.digit2, this.digit3);
	  }
}
