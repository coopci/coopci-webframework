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

	@Test
	public void testEmbedUserCode() throws IOException {
		TableClassGenerator generator = new TableClassGenerator();

		String oldSourceCode = "line1\r\n"
				+ "line2\r\n"
				+ "/////////////////// User code begins here /////////////////\r\n"
				+ "    usercode1\r\n"
				+ "usercode2\r\n"
				+ "/////////////////// User code ends here /////////////////\r\n"
				+ "line-2\r\n" + "line-1";
		ByteArrayInputStream ins = new ByteArrayInputStream(
				oldSourceCode.getBytes());

		String newSourceCode = "newline1\r\n"
				+ "newline2\r\n"
				+ "/////////////////// User code begins here /////////////////\r\n"
				+ "/////////////////// User code ends here /////////////////\r\n"
				+ "newline-2\r\n" + "newline-1";

		String embeded = generator.embedUserCode(ins, newSourceCode);
		System.out.println(embeded);
		assertEquals(
				"newline1\r\n"
						+ "newline2\r\n"
						+ "/////////////////// User code begins here /////////////////\r\n"
						+ "    usercode1\r\n"
						+ "usercode2\r\n"
						+ "/////////////////// User code ends here /////////////////\r\n"
						+ "newline-2\r\n" + "newline-1\r\n", embeded);

	}

}
