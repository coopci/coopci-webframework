package gubo.postman.parse;

import static org.junit.Assert.assertEquals;

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
		
		// 验证目录相同的情况下，是否是同一对象
		assertEquals(item1, item2);
		// 验证ItemGroup的父目录相同的情况下是不是同一对象
		assertEquals(fac.getParentItemGroup(item1), fac.getParentItemGroup(item4));
		// 验证和已有目录的父目录一样的情况下，是否是同一对象
		assertEquals(fac.getParentItemGroup(item4), item3);

	}

}
