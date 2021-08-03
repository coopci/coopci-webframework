package springless.auth.totp;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base32;

public class KeyGenerator {

	
	public String generateSecret(int secretSize, int numOfScratchCodes, int scratchCodeSie) {
		// Allocating the buffer
		byte[] buffer = new byte[secretSize + numOfScratchCodes * scratchCodeSie];

		// Filling the buffer with random numbers.
		// Notice: you want to reuse the same random generator
		// while generating larger random number sequences.
		new Random().nextBytes(buffer);
		// Getting the key and converting it to Base32
		Base32 codec = new Base32();
		byte[] secretKey = Arrays.copyOf(buffer, secretSize);
		byte[] bEncodedKey = codec.encode(secretKey);
		String encodedKey = new String(bEncodedKey);
		return encodedKey;
	}
	/**
	   *  产生一个随机的secret
	 **/
	public String generateSecret() {
		return generateSecret(10, 10, 10);
	}

	/**
	 *	 产生一个 otpauth://totp 的 url，这个url的二维码要给用户用google authenticator扫。
	 * 
	 **/
	public String generateUrl(String secret, String user, String host) {
		String format = "otpauth://totp/%s@%s?secret=%s";
		return String.format(format, user, host, secret);
	}
	
	/**
	 *	这个方法是用来检查用户提交的code是不是正确的对外接口 。 
	 *  @param secret 数据库里保存的用户的secret。
	 *  @param 用户在google authenticator里看到的数字，需要用户提交上来。
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 **/
	public static boolean checkCode(String secret, long code) throws InvalidKeyException, NoSuchAlgorithmException {
		long t = System.currentTimeMillis();
		t = t/30000;
		return checkCode(secret, code, t);
	}
	public static boolean checkCode(String secret, long code, long t)
			throws NoSuchAlgorithmException, InvalidKeyException {
		Base32 codec = new Base32();
		byte[] decodedKey = codec.decode(secret);

		// Window is used to check codes generated in the near past.
		// You can use this value to tune how far you're willing to go.
		int window = 3;
		for (int i = -window; i <= window; ++i) {
			long hash = verifyCode(decodedKey, t + i);

			if (hash == code) {
				return true;
			}
		}

		// The validation code is invalid.
		return false;
	}

	private static int verifyCode(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
		byte[] data = new byte[8];
		long value = t;
		for (int i = 8; i-- > 0; value >>>= 8) {
			data[i] = (byte) value;
		}

		SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(signKey);
		byte[] hash = mac.doFinal(data);

		int offset = hash[20 - 1] & 0xF;

		// We're using a long because Java hasn't got unsigned int.
		long truncatedHash = 0;
		for (int i = 0; i < 4; ++i) {
			truncatedHash <<= 8;
			// We are dealing with signed bytes:
			// we just keep the first byte.
			truncatedHash |= (hash[offset + i] & 0xFF);
		}

		truncatedHash &= 0x7FFFFFFF;
		truncatedHash %= 1000000;

		return (int) truncatedHash;
	}
}
