package gubo.postman;

import java.util.List;

/**
 * 根据“https://www.postmanlabs.com/postman-collection/tutorial-concepts.html”
 * 官方文档的Request建立。
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
