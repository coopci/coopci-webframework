package springless.jsonrpc;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Demo {
	protected static Logger logger = LoggerFactory.getLogger(Demo.class);

	public static void main(String[] args) throws Exception {
		JsonRpcClient jsonRpcClient = new JsonRpcClient();
		JsonRpcResult<Map> result = jsonRpcClient.invoke("http://localhost:8332/", "omnirpcuser", "123456",
				"omni_listtransactions", 1, Map.class);
		System.out.println("server response: ");
		System.out.println(result.getRawResult());
		return;
	}

	public static void main3(String[] args) throws Exception {
		String url = "http://localhost:8332/";
		String method = "omni_listtransactions";
		int id = 1;
		HashMap<String, Object> req = new HashMap<>();
		req.put("method", method);
		req.put("id", id);
		ObjectMapper om = new ObjectMapper();
		String data = om.writeValueAsString(req);

		BasicAuthenticator basicAuthenticator = new BasicAuthenticator("omnirpcuser", "123456");
		try {
			HttpResponse res = org.apache.http.client.fluent.Request.Post(url)
					.addHeader("Authorization", basicAuthenticator.getValue())
					.bodyByteArray(data.getBytes("utf-8"), ContentType.APPLICATION_JSON).connectTimeout(10000)
					.socketTimeout(10000).execute().returnResponse();

			int statusCode = res.getStatusLine().getStatusCode();
			byte[] serializedObject = EntityUtils.toByteArray(res.getEntity());
			String content = new String(serializedObject);
			if (statusCode != 200) {
				logger.info("server code: {}, response: {}", statusCode, content);
			}

			System.out.println("server response: ");
			System.out.println(content);
			// CibBalanceResponse response = om.readValue(content);

			Map result = (Map) om.readValue(content, Map.class);

			return;
		} catch (Exception e) {
			logger.error("Query order, request: " + data, e);
			throw e;
		}
	}

}
