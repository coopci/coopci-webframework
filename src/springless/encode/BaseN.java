package springless.encode;

public class BaseN {
	private final String alphabeta;
	private final int base;

	public BaseN(String alphabeta) {
		// TODO check there is no dup in alphabeta.
		this.alphabeta = alphabeta;
		this.base = alphabeta.length();
	}

	/**
	 * Encodes a decimal value to a Base62 <code>String</code>.
	 * 
	 * @param b10
	 *            the decimal value to encode, must be nonnegative.
	 * @return the number encoded as a Base62 <code>String</code>.
	 */
	public String encodeBase10(long b10) {
		if (b10 < 0) {
			throw new IllegalArgumentException("b10 must be nonnegative");
		}
		if (b10==0) {
			return "0";
		}
		String ret = "";
		while (b10 > 0) {
			ret = alphabeta.charAt((int) (b10 % this.base)) + ret;
			b10 /= this.base;
		}
		return ret;

	}

	/**
	 * Decodes a Base62 <code>String</code> returning a <code>long</code>.
	 * 
	 * @param b62
	 *            the Base62 <code>String</code> to decode.
	 * @return the decoded number as a <code>long</code>.
	 * @throws IllegalArgumentException
	 *             if the given <code>String</code> contains characters not
	 *             specified in the constructor.
	 */
	public long decodeBase62(String b62) {
		for (char character : b62.toCharArray()) {
			if (!alphabeta.contains(String.valueOf(character))) {
				throw new IllegalArgumentException("Invalid character(s) in string: " + character);
			}
		}
		long ret = 0;
		b62 = new StringBuffer(b62).reverse().toString();
		long count = 1;
		for (char character : b62.toCharArray()) {
			ret += alphabeta.indexOf(character) * count;
			count *= this.base;
		}
		return ret;
	}

	// Examples
	public static void main(String[] args) throws InterruptedException {
		// Create a Base62 object using the default charset.
		BaseN base62 = new BaseN("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
		System.gc();
		// Create a Base62 object with an alternate charset.
		BaseN base62alt = new BaseN("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");

		// Convert 1673 to Base62 (qZ).
		System.out.println("1673 encoded to Base62: " + base62.encodeBase10(1673));

		// Convert 1673 to Base62 with the alternate character set (A9).
		System.out.println("1673 encoded with alternate charset: " + base62alt.encodeBase10(1673));

		// Convert nHkl3S4B to decimal (83,458,179,955,437).
		System.out.println("nHkl3S4B decoded from Base62: " + base62.decodeBase62("nHkl3S4B"));

		// Encoding and decoding a number returns the original result.
		System.out.println("32442342 encoded to Base62 and back again: "
				+ base62.decodeBase62(base62.encodeBase10(32442342)));

		// Using invalid characters throws a runtime exception.
		// Output was out of order with ant, adding this short sleep fixes
		// things:
		// The problem seems to be with the way ant's output handles system.err
		Thread.sleep(100);
		try {
			// Doesn't work
			System.out.println(base62.decodeBase62("_j+j%"));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

	}
}