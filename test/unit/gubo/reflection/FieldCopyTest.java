package gubo.reflection;

import static org.junit.Assert.*;

import org.junit.Test;

public class FieldCopyTest {

	
	class FromClass {
		String field1 = "field1";
		int field2 = 12;
		String field3 = "field3";
		String field4 = "field4";
		
		String field5 = "field5";
	}
	
	class BaseToClass {
		
		String field4;
	}
	class ToClass extends BaseToClass {
		String field1 = "";
		int field2 = 0;
		long field3 = 999;
	}
	
	@Test
	public void testCopy() throws IllegalArgumentException, IllegalAccessException {
		FromClass from = new FromClass();
		ToClass to = new ToClass();
		FieldCopy.copy(from, to);
		
		assertEquals("field1", to.field1);
		assertEquals(12, to.field2);
		assertEquals(999, to.field3);
		assertEquals("field4", to.field4);
	}
}
