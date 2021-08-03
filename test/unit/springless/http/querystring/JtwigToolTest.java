package springless.http.querystring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.jtwig.JtwigModel;
import org.junit.Test;

import springless.http.querystring.JtwigTool;
import springless.http.querystring.QueryStringField;

public class JtwigToolTest {

	public class TestPojo {

		@QueryStringField()
		public String field1;

		@QueryStringField(name = "field_2")
		String field2;
	}

	@Test
	public void testPopulate() throws IllegalArgumentException,
			IllegalAccessException {

		JtwigTool t = new JtwigTool();
		JtwigModel model = new JtwigModel();

		TestPojo pojo = new TestPojo();

		pojo.field1 = "aaa";
		pojo.field2 = "bb";
		t.populateJtwigModel(model, pojo);

		assertEquals("aaa", model.get("field1").get().getValue());

		assertEquals("bb", model.get("field_2").get().getValue());
		assertFalse(model.get("field2").isPresent());

	}
}
