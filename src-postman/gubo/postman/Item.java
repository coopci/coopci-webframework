package gubo.postman;

import java.util.List;

/**
 * 根据“https://www.postmanlabs.com/postman-collection/tutorial-concepts.html”
 * 文档的Item建立。
 *
 */
public class Item {
	public String name;
	public Request request;
	public List<?> response;
}
