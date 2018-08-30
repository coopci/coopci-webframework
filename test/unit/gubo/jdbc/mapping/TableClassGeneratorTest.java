package gubo.jdbc.mapping;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

public class TableClassGeneratorTest {

	@Test
	public void testExtractUserCode() throws IOException {
		TableClassGenerator generator = new TableClassGenerator();

		String sourceCode = "line1\r\n"
				+ "line2\r\n"
				+ "/////////////////// User code begins here /////////////////\r\n"
				+ "    usercode1\r\n"
				+ "usercode2\r\n"
				+ "/////////////////// User code ends here /////////////////\r\n"
				+ "line-2\r\n" + "line-1";
		ByteArrayInputStream ins = new ByteArrayInputStream(
				sourceCode.getBytes());
		String extracted = generator.extractUserCode(ins);
		assertEquals("    usercode1\r\n" + "usercode2\r\n", extracted);
	}
}
