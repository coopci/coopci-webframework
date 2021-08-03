package springless.jsonrpc;

import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonRpcClient {
	protected static Logger logger = LoggerFactory.getLogger(JsonRpcClient.class);
	public <T> JsonRpcResult<T> invoke(String url, String username, String password, String method, int id, Class<T> clazz, Object ... args) throws JsonProcessingException {
		BasicAuthenticator basicAuthenticator = new BasicAuthenticator(username, password);
		HashMap<String, Object> req = new HashMap<>();
		req.put("method", method);
		req.put("id", id);
		req.put("params", args);
		ObjectMapper om = new ObjectMapper();
		String data = om.writeValueAsString(req);
		try {
			HttpResponse res = org.apache.http.client.fluent.Request.Post(url)
					.addHeader("Authorization", basicAuthenticator.getValue())
					.bodyByteArray(data.getBytes("utf-8"), ContentType.APPLICATION_JSON).connectTimeout(10000)
					.socketTimeout(10000).execute().returnResponse();

			
			int statusCode = res.getStatusLine().getStatusCode();
			byte[] serializedObject = EntityUtils.toByteArray(res.getEntity());
			String content = new String(serializedObject);
			content = content.trim();
			if (statusCode != 200) {
				logger.info("server code: {}, response: {}", statusCode, content);
			} else {
				// logger.info("server response: {}", content);
			}


			T result = (T)om.readValue(content, clazz);
			
			return new JsonRpcResult<>(result, content);
			
		} catch (Throwable t) {
			logger.error("invoke order, request: " + data, t);
			throw new RuntimeException(t);
		}
	} 
}
