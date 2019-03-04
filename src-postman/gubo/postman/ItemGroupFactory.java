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
	 * 获取或者创建ItemGroup。 对于一个"A/B"，首先判断是不是已经有"A/B"了，如果有就返回已经有的这个"A/B"， 否则就新建"A/B"。
	 * getOrCreateGroup("A/B")的时候调用 getOrCreateGroup("A") ，得到A后把A/B放到A里。
	 * 
	 * @param groupPath
	 * @return
	 */
	public ItemGroup getOrCreateItemGroup(String groupPath) {
		if (cachedItemGroupMap.containsKey(groupPath)) {
			return cachedItemGroupMap.get(groupPath);
		}

		String[] groupPathList = groupPath.split("/");
		ItemGroup postmanItem;
		if (groupPathList.length < 2) {
			postmanItem = new ItemGroup(groupPathList[0], false);
		} else {
			postmanItem = new ItemGroup(groupPathList[groupPathList.length - 1], true);
		}
		if (groupPathList.length > 1) {
			String parentPath = postmanItem.getParentPath(groupPath);
			getOrCreateItemGroup(parentPath);
		}
		cachedItemGroupMap.put(groupPath, postmanItem);
		ItemGroup itemGroup = generateItemGroup(groupPath);
		return itemGroup;

	}

	/**
	 * 实现把A/B放到A里这一步。比如："A/B/C"就将"A/B/C"放进"A/B"里，再把"A/B"放进"A"里。
	 * 
	 * @param groupPath
	 * @return
	 */
	public ItemGroup generateItemGroup(String groupPath) {
		for (String groupOne : cachedItemGroupMap.keySet()) {
			for (String groupTwo : cachedItemGroupMap.keySet()) {
				String[] groupOneList = groupOne.split("/");
				String[] groupTwoList = groupTwo.split("/");
				if (groupOneList.length - groupTwoList.length == 1) {
					if (groupOne.substring(0, groupTwo.length()).equals(groupTwo)) {
						cachedItemGroupMap.get(groupTwo).item.add(cachedItemGroupMap.get(groupOne));
					}
				}
			}
		}
		return cachedItemGroupMap.get(groupPath);
	}

	public ItemGroup getItemGroup(String groupPath) {
		return cachedItemGroupMap.get(groupPath);
	}

}
