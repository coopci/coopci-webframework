package gubo.postman;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

/**
 * Based on
 * “https://www.postmanlabs.com/postman-collection/tutorial-concepts.html”. An
 * ItemGroup is a simple container for Items. Literally, a Collection is just an
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
	// 存放ItemGroup或者Request
	public List<Object> item;

	public ItemGroup() {
		this.item = new LinkedList<>();
	}

	public ItemGroup(String name, boolean isSub) {
		this.name = name;
		this._postman_isSubFolder = isSub;
		this.item = new LinkedList<>();
	}

	public boolean hasChild(ItemGroup item) {
		ItemGroupFactory fac = new ItemGroupFactory();
		String itemGroupPath = "";
		// 从cachedItemGroupMap中获取item对应的键
		for (Entry<String, ItemGroup> m : fac.getCachedItemGroupMap().entrySet()) {
			if (m.getValue().equals(item)) {
				itemGroupPath = m.getKey();
			}
		}
		//获取item上级目录的路径
		String[] itemGroupPathList = itemGroupPath.split("/");
		StringBuffer groupStr = new StringBuffer();
		for (int i = 0; i < itemGroupPathList.length - 1; i++) {
			groupStr.append(itemGroupPathList[i] + "/");
		}
		groupStr.setLength(groupStr.length() - 1);
		
		String thisGroupPath = "";
		// 从cachedItemGroupMap中获取该对象对应的键
		for (Entry<String, ItemGroup> m : fac.getCachedItemGroupMap().entrySet()) {
			if (m.getValue().equals(this)) {
				thisGroupPath = m.getKey();
			}
		}
		
		ItemGroup itemObj = fac.getCachedItemGroupMap().get(groupStr.toString());
		ItemGroup thisObj = fac.getCachedItemGroupMap().get(thisGroupPath);
		if (itemObj == thisObj) {
			return true;
		} else {
			return false;
		}
	}

}
