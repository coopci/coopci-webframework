package gubo.http.clients;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import springless.http.clients.SimpleHttpClient;
import springless.http.querystring.demo.Person;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SimpleHttpClientTest {

	@Test
	public void testPost() throws IOException, InterruptedException,
			IllegalArgumentException, IllegalAccessException {
		Person person = new Person();
		person.age = 100;
		ObjectMapper om = new ObjectMapper();
		MockWebServer server = new MockWebServer();

		server.enqueue(new MockResponse().setBody(om.writeValueAsString(person)));
		server.enqueue(new MockResponse().setBody(om.writeValueAsString(person)));
		server.start();

		SimpleHttpClient testee = new SimpleHttpClient();

		Person res = (Person) testee.post(server.url("/").url().toString(),
				person, Person.class);
		assertEquals(100, res.age);

		RecordedRequest request1 = server.takeRequest();
		String resBody = request1.getBody().readString(Charset.forName("utf-8"));
		
		assertTrue(resBody.contains("register_time=&"));
		assertTrue(resBody.contains("tier=0&"));
		assertTrue(resBody.contains("height=0.0"));
		assertTrue(resBody.contains("age=100&"));
		assertTrue(resBody.contains("isVIP=false&"));
		assertTrue(resBody.contains("name=&"));
			
		server.shutdown();
		server.close();
	}
}
