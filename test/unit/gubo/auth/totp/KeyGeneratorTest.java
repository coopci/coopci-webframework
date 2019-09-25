package gubo.auth.totp;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import gubo.auth.totp.KeyGenerator;

public class KeyGeneratorTest {
	@Test
	public void testGenerate() {
		KeyGenerator testee = new KeyGenerator();
		String secret = testee.generateSecret();
		String url = testee.generateUrl(secret, "user1", "host1");
		System.out.println("secret="+secret);
		System.out.println("url="+url);
		
	}
	
	@Test
	public void testCheck() throws InvalidKeyException, NoSuchAlgorithmException {
		
		//long t = System.currentTimeMillis();
		//t = t/30000;
		
		long t = 52313934;
		long code = 627290;
		boolean result = KeyGenerator.checkCode("OKDBSXOETZPWRS6J",  code, t);
		System.out.println("t="+t);
		System.out.println("code="+code);
		System.out.println("secret=OKDBSXOETZPWRS6J"+t);
		System.out.println("result="+result);
	}
	
	@Test
	public void testCheckWithoutT() throws InvalidKeyException, NoSuchAlgorithmException {
		
		long code = 838657;
		boolean result = KeyGenerator.checkCode("OKDBSXOETZPWRS6J",  code);
		// System.out.println("t="+t);
		System.out.println("code="+code);
		System.out.println("secret=OKDBSXOETZPWRS6J");
		System.out.println("result="+result);
	}
}
