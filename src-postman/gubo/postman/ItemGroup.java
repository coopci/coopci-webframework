package gubo.postman;

import java.util.LinkedList;
import java.util.List;

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
	// 判断是否是子目录
	public boolean _postman_isSubFolder;
	// 存放ItemGroup或者Request
	public List<Object> item;

	public ItemGroup() {
		this.item = new LinkedList<>();
	}

	public ItemGroup(String groupPath) {
		String[] groupList = groupPath.split("/");
		if (isTopLevel(groupPath)) {
			this.name = groupList[0];
			this._postman_isSubFolder = false;
		} else {
			this.name = groupList[groupList.length - 1];
			this._postman_isSubFolder = true;
		}
		this.item = new LinkedList<>();
	}

	public boolean hasChild(ItemGroup item) {
		// 一个文件夹下可能有多个文件夹，所以需要循环判断
		for (int i = 0; i < this.item.size(); i++) {
			if (this.item.get(i).equals(item)) {
				return true;
			}
		}
		return false;
	}

	// 获取路径的上级路径
	public static String getParentPath(String groupPath) {
		String[] groupPathList = groupPath.split("/");
		StringBuffer groupStr = new StringBuffer();
		for (int i = 0; i < groupPathList.length - 1; i++) {
			groupStr.append(groupPathList[i] + "/");
		}
		groupStr.setLength(groupStr.length() - 1);
		return groupStr.toString();
	}

	// 判断是否为顶层目录
	public static boolean isTopLevel(String groupPath) {
		String[] groupList = groupPath.split("/");
		if (groupList.length < 2) {
			return true;
		} else {
			return false;
		}
	}

}
