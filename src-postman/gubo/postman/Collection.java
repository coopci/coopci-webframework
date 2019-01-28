package gubo.postman;

import java.util.List;

/**
 * Postman collection.
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
