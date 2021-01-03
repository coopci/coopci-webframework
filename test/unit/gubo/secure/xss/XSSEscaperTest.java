package gubo.secure.xss;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class XSSEscaperTest {
	
	public static class User {
		String name;
		String desc;
		
	}
	@Test
	public void testescapeHtml() throws IllegalArgumentException, IllegalAccessException {
		User u = new User();
		u.desc = "<sCrIpt>xs.sb/LK51</script>";
		XSSEscaper.escapeHtml(u);
		assertEquals("&lt;sCrIpt&gt;xs.sb&#x2F;LK51&lt;&#x2F;script&gt;",u.desc);
	} 
	

}
