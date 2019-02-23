package gubo.postman;

import java.util.LinkedList;
import java.util.List;

import gubo.http.querystring.QueryStringField;

public class ItemGroup {
	// 文件夹名称
	public String name;
	// 文件夹描述
	public String description;
	// 判断是否是子文件夹
	public boolean _postman_isSubFolder;
	public List<Object> item;

	public void setItem(ItemGroup item) {
		List<Object> itemList = new LinkedList<>();
		itemList.add(item);
		this.item = itemList;
	}
	
	public List<Object> getItem() {
		return this.item;
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
