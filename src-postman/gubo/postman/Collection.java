package gubo.postman;

import java.util.List;

/**
 * Based on “https://www.postmanlabs.com/postman-collection/tutorial-concepts.html”.
 * A collection can contain a number of Items, ItemGroups and can have a single information block.
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
	public List<?> item;
	
}
