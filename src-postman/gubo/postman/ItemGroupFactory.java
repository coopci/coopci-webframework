package gubo.postman;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

/**
 * 根据“https://www.postmanlabs.com/postman-collection/tutorial-concepts.html”
 * 文档的ItemGroup建立,用以生成Collection中ItemGroup的json结构。
 *
 */
public class ItemGroupFactory {
	private HashMap<String, ItemGroup> cachedItemGroupMap = new HashMap<String, ItemGroup>();

	/**
	 * 获取或者创建ItemGroup。 对于一个"A/B"，首先判断是不是已经有"A/B"了，如果有就返回已经有的这个"A/B"， 否则就新建"A/B"。
	 * 
	 * @param groupPath
	 * @return
	 */
	public ItemGroup getOrCreateItemGroup(String groupPath) {
		if (cachedItemGroupMap.containsKey(groupPath)) {
			return cachedItemGroupMap.get(groupPath);
		}
		String[] groupPathList = groupPath.split("/");
		ItemGroup postmanItem = generateItemGroup(groupPathList);
		cachedItemGroupMap.put(groupPath, postmanItem);

		StringBuffer groupStr = new StringBuffer();
		if (groupPathList.length > 1) {
			for (int i = 0; i < groupPathList.length - 1; i++) {
				groupStr.append(groupPathList[i] + "/");
			}
			groupStr.setLength(groupStr.length() - 1);
			cachedItemGroupMap(postmanItem, groupPath);
			getOrCreateItemGroup(groupStr.toString());
		}
		return postmanItem;
	}

	public ItemGroup generateItemGroup(String[] groupPathList) {
		ItemGroup childItem;
		ItemGroup parentItem;
		if (groupPathList.length < 2) {
			parentItem = new ItemGroup(groupPathList[0], false);
		} else {
			childItem = new ItemGroup(groupPathList[groupPathList.length - 1], true);
			parentItem = new ItemGroup(groupPathList[groupPathList.length - 2], true);
			parentItem.item = new LinkedList<Object>();
			parentItem.item.add(childItem);
		}

		return parentItem;
	}

	private String cachedItemGroupMap(ItemGroup itemObj, String groupPathKey) {
		if (cachedItemGroupMap.containsKey(groupPathKey)) {
			return groupPathKey;
		}
		cachedItemGroupMap.put(groupPathKey, itemObj);
		return groupPathKey;
	}

	public ItemGroup getItemGroup(String groupPath) {
		return cachedItemGroupMap.get(groupPath);
	}

	public ItemGroup getParentItemGroup(ItemGroup item) {
		String key = "";
		for (Entry<String, ItemGroup> m : cachedItemGroupMap.entrySet()) {
			if (m.getValue().equals(item)) {
				key = m.getKey();
			}
		}
		StringBuffer groupStr = new StringBuffer();
		String[] groupPathList = key.split("/");
		for (int i = 0; i < groupPathList.length - 1; i++) {
			groupStr.append(groupPathList[i] + "/");
		}
		groupStr.setLength(groupStr.length() - 1);
		
		return cachedItemGroupMap.get(groupStr.toString());
	}

}
