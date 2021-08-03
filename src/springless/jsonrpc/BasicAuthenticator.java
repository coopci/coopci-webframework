package springless.jsonrpc;

import java.net.HttpURLConnection;
import java.util.Base64;

import com.thetransactioncompany.jsonrpc2.client.ConnectionConfigurator;

public class BasicAuthenticator implements ConnectionConfigurator {

	private final String username;
	private final String password;
	private final String value;
	public BasicAuthenticator(String username, String password) {
		this.username = username;
		this.password = password;
		
		
		value = "Basic " + Base64.getEncoder().encodeToString((this.username + ":" + this.password).getBytes());
		
			
	}
	public void configure(HttpURLConnection connection) {
	
		// add custom HTTP header
		connection.addRequestProperty("Authorization", this.value);
	}
	
	public String getValue() {
		return value;
	}
}