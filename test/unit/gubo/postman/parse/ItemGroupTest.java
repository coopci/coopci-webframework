package gubo.postman.parse;

import static org.junit.Assert.*;

import org.junit.Test;

import gubo.postman.ItemGroup;

public class ItemGroupTest {

	@Test
	public void testGetParentPath() {
		String parentPath = ItemGroup.getParentPath("A/B/C");
		assertEquals("A/B", parentPath);
		
		// 验证各层级之间如果有多个"/"，不会影响getParentPath()和isTopLevel()
		String parentPath1 = ItemGroup.getParentPath("A///B///C");
		assertEquals("A///B//", parentPath1);
		String parentPath2 = ItemGroup.getParentPath(parentPath1);
		assertEquals("A//", parentPath2);
		assertTrue(ItemGroup.isTopLevel(parentPath2));
		
		// 验证路径结尾可以有"/"
		String parentPath3 = ItemGroup.getParentPath("A/B/C/");
		assertEquals("A/B", parentPath3);
		
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
