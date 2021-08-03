package springless.auth.totp;

// 表示一个可以用totp验证的身份。
public interface TotpIdentity {
	public String getUser();
	public void setUser(String user);
	public String getHost();
	public void setHost(String host);
	public String getSecret();
	public void setSecret(String secret);
}
