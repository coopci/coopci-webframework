package gubo.postman;

import java.util.HashMap;

/**
 * 根据“https://www.postmanlabs.com/postman-collection/tutorial-concepts.html”
 * 文档的ItemGroup建立,用以生成Collection中文件夹形式的json结构。
 *
 */
public class ItemGroupFactory {
	private HashMap<String, ItemGroup> cachedItemGroupMap = new HashMap<String, ItemGroup>();
	private ItemGroup itemObj = new ItemGroup();

	/**
	 * 获取或者创建ItemGroup。
	 * 对于一个"A/B"，首先判断是不是已经有"A/B"了，如果有就返回已经有的这个"A/B"， 否则就新建"A/B"。
	 * @param groupPath
	 * @return
	 */
	public ItemGroup getOrCreateItemGroup(String groupPath) {
		if (cachedItemGroupMap.containsKey(groupPath)) {
			return cachedItemGroupMap.get(groupPath);
		}
		String[] groupList = groupPath.split("/");
		generateItemGroup(groupList, groupList.length - 1);
		cachedItemGroupMap.put(groupPath, itemObj);
		return itemObj;
	}

	private void generateItemGroup(String[] groupList, int n) {
		boolean isSub;
		if (n == 0) {
			isSub = false;
		} else {
			isSub = true;
		}
		ItemGroup parentItem = new ItemGroup(groupList[n], isSub);
		if (n < groupList.length - 1) {
			parentItem.setItem(itemObj);
		}
		itemObj = parentItem;
		if (n < 1) {
			return;
		}
		generateItemGroup(groupList, n - 1);
	}

}
