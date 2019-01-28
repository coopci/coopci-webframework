package gubo.postman;

import java.util.List;

/**
 * 根据“https://www.postmanlabs.com/postman-collection/tutorial-concepts.html”官方文档。
 *
 */
public class Collection {
	public static class Information {
		public String name;
		public String _postman_id;
		public String description;
		public String schema;
	}
	
	public Information info;
	public List<Item> item;
	
}
