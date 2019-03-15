package gubo.postman;

import java.util.List;

/**
 * Based on “https://www.postmanlabs.com/postman-collection/tutorial-concepts.html”. 
 * A Request represents an HTTP request. Usually, a Request belongs to an Item.
 * Requests can be specified as a string (check the example above) or as a JSON
 * Object. If specified as a string, it is assumed to be a GET request.
 *
 */
public class Request {
	public String url;
	public String method;
	public String description;

	public List<Header> header;
	public Body body;

	public static class Header {
		public String key;
		public String value;
		public String description;
	}

	public static class Body {
		public String mode;
		public List<Urlencoded> urlencoded;

		public static class Urlencoded {
			public String key;
			public String value;
			public String description;
			public String type = "text";
		}
		
	}

}
