package gubo.postman.parse;

import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import gubo.postman.ItemGroup;
import gubo.postman.ItemGroupFactory;

public class ItemGroupFactoryTest {

	@Test
	public void testGetOrCreateItemGroup_Same_Object() throws JsonProcessingException {
		List<String> pathList = new LinkedList<String>();
		pathList.add("A/B/C");
		pathList.add("A/B/C");
		pathList.add("一/二/三");

		ItemGroupFactory fac = new ItemGroupFactory();
		ItemGroup item = new ItemGroup();
		for (String path : pathList) {
			item = fac.getOrCreateItemGroup(path);
			System.out.println(item);
			assertNotNull(item);
		}

	}

}
