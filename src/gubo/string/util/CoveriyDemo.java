package gubo.string.util;

import com.coverity.security.Escape;
public class CoveriyDemo {

	
	public static void main(String[] args) {
		String xssString = "<sCRiPt sRC=//xs.sb/LK51></sCrIpT>";
		System.out.println(Escape.htmlText(xssString));
		System.out.println(Escape.cssString(xssString));
	}
}
