package gubo.postman;

import java.util.LinkedList;
import java.util.List;

public class ItemGroup {
	public String name;
	public String description;
	public boolean _postman_isSubFolder;
	public List<Object> item;

	public void setItem(ItemGroup item) {
		List<Object> itemList = new LinkedList<>();
		itemList.add(item);
		this.item = itemList;
	}
	
	public ItemGroup() {
		this.item = new LinkedList<>();
	}

	public ItemGroup(String name, boolean isSub) {
		this.name = name;
		this._postman_isSubFolder = isSub;
		this.item = new LinkedList<>();
	}

}
