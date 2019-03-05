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

	public ItemGroup(String name, boolean isSub) {
		this.name = name;
		this._postman_isSubFolder = isSub;
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
	static boolean isTopLevel(String[] groupList) {
		if (groupList.length < 2) {
			return true;
		}else {
			return false;
		}
	}

}
