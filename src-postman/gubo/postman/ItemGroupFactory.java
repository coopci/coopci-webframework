package gubo.postman;

import java.util.HashMap;

/**
 * 根据“https://www.postmanlabs.com/postman-collection/tutorial-concepts.html”
 * 文档的ItemGroup建立,用以生成Collection中ItemGroup的json结构。
 *
 */
public class ItemGroupFactory {
	/**
	 * 存放路径及其对应的ItemGroup对象；对于一个"A/B/C"，就会有"A","A/B","A/B/C"及其对应的ItemGroup
	 */
	private HashMap<String, ItemGroup> cachedItemGroupMap = new HashMap<String, ItemGroup>();

	public HashMap<String, ItemGroup> getCachedItemGroupMap() {
		return cachedItemGroupMap;
	}

	/**
	 * 获取或者创建ItemGroup。 对于一个"A/B"，首先判断是不是已经有"A/B"了，如果有就返回已经有的这个"A/B"。 否则就新建"A/B"， 调用
	 * getOrCreateGroup("A") ，得到A后把A/B放到A里。
	 * 
	 * @param groupPath
	 * @return
	 */
	public ItemGroup getOrCreateItemGroup(String groupPath) {
		// 如果groupPath已存在，则直接返回cachedItemGroupMap里缓存的对象
		if (cachedItemGroupMap.containsKey(groupPath)) {
			return cachedItemGroupMap.get(groupPath);
		}

		String[] groupList = groupPath.split("/");
		ItemGroup childItemGroup;
		ItemGroup parentItemGroup;

		if (ItemGroup.isTopLevel(groupList)) {
			childItemGroup = new ItemGroup(groupList[0], false);
		} else {
			childItemGroup = new ItemGroup(groupList[groupList.length - 1], true);
		}

		if (!ItemGroup.isTopLevel(groupList)) {
			String parentPath = ItemGroup.getParentPath(groupPath);
			parentItemGroup = getOrCreateItemGroup(parentPath);
			parentItemGroup.item.add(childItemGroup);
			cachedItemGroupMap.put(groupPath, childItemGroup);
			return childItemGroup;
		} else {
			cachedItemGroupMap.put(groupPath, childItemGroup);
			return childItemGroup;
		}

	}

	public ItemGroup getItemGroup(String groupPath) {
		return cachedItemGroupMap.get(groupPath);
	}

}
