package gubo.postman;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import gubo.postman.ItemGroup;
import gubo.postman.ItemGroupFactory;

public class ItemGroupFactoryTest {

	@Test
	public void testGetOrCreateItemGroup() throws JsonProcessingException {
		ItemGroupFactory fac = new ItemGroupFactory();

		ItemGroup item1 = fac.getOrCreateItemGroup("A/B/C");
		ItemGroup item2 = fac.getOrCreateItemGroup("A/B/C");
		ItemGroup item3 = fac.getOrCreateItemGroup("A/B");
		ItemGroup item4 = fac.getOrCreateItemGroup("A/B/D");
		ItemGroup item5 = fac.getOrCreateItemGroup("A");
		// 验证目录相同的情况下，是否是同一对象
		assertEquals(item1, item2);

		// 验证某一目录是否是另一目录的下级目录
		assertTrue(item3.hasChild(item1));
		assertTrue(item3.hasChild(item2));
		assertTrue(item3.hasChild(item4));
		assertTrue(item5.hasChild(item3));
		assertFalse(item5.hasChild(item1));
		assertFalse(item5.hasChild(item2));
		assertFalse(item5.hasChild(item4));
		assertFalse(item1.hasChild(item4));

		ItemGroupFactory fac2 = new ItemGroupFactory();
		ItemGroup item6 = fac2.getOrCreateItemGroup("A/B/C");
		ItemGroup item7 = fac2.getOrCreateItemGroup("A/B/D");
		ItemGroup item8 = fac2.getOrCreateItemGroup("A");

		// 验证不同ItemGroupFactory对于同一path生成的ItemGroup实际上是不一样的
		assertNotEquals(item1, item6);
		assertNotEquals(item4, item7);
		assertNotEquals(item5, item8);

	}
	
}
