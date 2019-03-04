package gubo.postman.parse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

	}

}
