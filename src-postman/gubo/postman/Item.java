package gubo.postman;

import java.util.List;

/**
 * Based on
 * “https://www.postmanlabs.com/postman-collection/tutorial-concepts.html”. 
 * An Item is the basic building block for a collection. It represents an HTTP
 * request, along with the associated metadata.
 *
 */
public class Item {
	public String name;
	public Request request;
	public List<?> response;
	
}
