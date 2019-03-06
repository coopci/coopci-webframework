package gubo.postman.parse;

import static org.junit.Assert.*;

import org.junit.Test;

import gubo.postman.ItemGroup;

public class ItemGroupTest {

	@Test
	public void testGetParentPath() {
		String groupPath = "A/B/C";
		String parentPath = ItemGroup.getParentPath(groupPath);
		assertEquals("A/B", parentPath);
		// 路径开头和结尾不能有“/”
		assertNotEquals("A/B/", parentPath);
		assertNotEquals("/A/B", parentPath);

		String parentPath2 = ItemGroup.getParentPath("A//B//C");
		// 路径中间只能有一个“/”
		assertNotEquals("A/B", parentPath2);
	}

	@Test
	public void testIsTopLevel() {
		assertTrue(ItemGroup.isTopLevel("A"));
		assertTrue(ItemGroup.isTopLevel("A/"));
		assertTrue(ItemGroup.isTopLevel("A//"));
		// 路径开头不能有“/”
		assertFalse(ItemGroup.isTopLevel("/A"));
		assertFalse(ItemGroup.isTopLevel("A/B/C"));
		assertFalse(ItemGroup.isTopLevel("A/B"));

	}

}
