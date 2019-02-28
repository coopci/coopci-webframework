package gubo.postman;

import java.util.LinkedList;
import java.util.List;

/**
 * Based on “https://www.postmanlabs.com/postman-collection/tutorial-concepts.html”. 
 * An ItemGroup is a simple container for Items. Literally, a Collection is just an
 * ItemGroup with a special information block.
 *
 */
public class ItemGroup {
	// 文件夹名称
	public String name;
	// 文件夹描述
	public String description;
	// 判断是否是子文件夹
	public boolean _postman_isSubFolder;
	public List<Object> item;

	public ItemGroup() {
		this.item = new LinkedList<>();
	}

	public ItemGroup(String name, boolean isSub) {
		this.name = name;
		this._postman_isSubFolder = isSub;
		this.item = new LinkedList<>();
	}
}
