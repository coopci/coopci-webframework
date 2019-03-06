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

	/**
	 * 获取或者创建ItemGroup。 对于一个"A/B"，首先判断是不是已经有"A/B"了，如果有就返回已经有的这个"A/B"。 否则就新建"A/B"， 调用
	 * getOrCreateItemGroup("A") ，得到A后把A/B放到A里。
	 * 
	 * @param groupPath
	 * @return
	 */
	public ItemGroup getOrCreateItemGroup(String groupPath) {
		// 如果groupPath已存在，则直接返回cachedItemGroupMap里缓存的对象
		if (cachedItemGroupMap.containsKey(groupPath)) {
			return cachedItemGroupMap.get(groupPath);
		}
		ItemGroup ret = new ItemGroup(groupPath);

		if (ItemGroup.isTopLevel(groupPath)) {
			cachedItemGroupMap.put(groupPath, ret);
			return ret;
		} else {
			String parentPath = ItemGroup.getParentPath(groupPath);
			ItemGroup parentItemGroup = getOrCreateItemGroup(parentPath);
			parentItemGroup.item.add(ret);
			cachedItemGroupMap.put(groupPath, ret);
			return ret;
		}

	}

}
