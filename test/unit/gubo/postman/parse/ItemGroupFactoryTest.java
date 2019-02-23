package gubo.postman.parse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import gubo.postman.ItemGroup;
import gubo.postman.ItemGroupFactory;

public class ItemGroupFactoryTest {

	@Test
	public void testGetOrCreateItemGroup() throws JsonProcessingException {
		List<String> pathList = new LinkedList<String>();
		pathList.add("A/B/C");
		pathList.add("A/B/C");
		pathList.add("A/B");

		ItemGroupFactory fac = new ItemGroupFactory();
		ItemGroup item1 = fac.getOrCreateItemGroup(pathList.get(0));
		ItemGroup item2 = fac.getOrCreateItemGroup(pathList.get(1));
		ItemGroup item3 = fac.getOrCreateItemGroup(pathList.get(2));
		// 验证目录相同的情况下，是否是同一对象
		assertEquals(item1, item2);
		// 验证和已有目录的父目录一样的情况下，是否是同一对象
		assertEquals(fac.getCachedItemGroupMap().get(pathList.get(2)), item3);
		
	}

}
