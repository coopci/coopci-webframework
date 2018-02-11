package gubo.password;

import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class SaltGenerator {

	public static String defaultAlphabeta = "0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
	
	public static String generate(int length) throws NoSuchAlgorithmException {
		Random rng = new Random();
		return generateString(rng, defaultAlphabeta, length); 
	}
	
	public static String generate(String characters, int length) throws NoSuchAlgorithmException {
		Random rng = new Random();
		return generateString(rng, characters, length); 
	}
	public static String generateString(Random rng, String characters, int length) {
		char[] text = new char[length];
		for (int i = 0; i < length; i++) {
			text[i] = characters.charAt(rng.nextInt(characters.length()));
		}
		return new String(text);
	}
	
	public static void main(String[] argv) throws Exception {
		for (int i = 0; i < 1000; ++i) {
			String salt = SaltGenerator.generate(12);
			System.out.println("salt: " + salt);
		}
		
	}
}
