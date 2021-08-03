package springless.http.grizzly.handlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;

import springless.http.grizzly.handlers.InMemoryMultipartEntryHandler;

public class InMemoryMultipartEntryHandlerTest {

	@Test
	public void testSizeLimit() {
		HashMap<String, Integer> sizeLimit = new HashMap<String, Integer>();
		sizeLimit.put("ignore-this",
				InMemoryMultipartEntryHandler.SIZE_LIMIT_IGNORE);
		sizeLimit.put("reject-this",
				InMemoryMultipartEntryHandler.SIZE_LIMIT_REJECT);
		sizeLimit.put("big-one", 1024 * 1024);
		final InMemoryMultipartEntryHandler testee = new InMemoryMultipartEntryHandler(
				sizeLimit);

		assertTrue(testee.checkIgnore("ignore-this"));

		boolean illegalArgumentExceptionThrown = false;
		try {
			testee.checkReject("reject-this");
		} catch (IllegalArgumentException ex) {
			illegalArgumentExceptionThrown = true;
			assertEquals("parameter reject-this is not allowed",
					ex.getMessage());
		}
		assertTrue(illegalArgumentExceptionThrown);
		assertEquals(1024 * 1024, testee.getEffecitveSizeLimit("big-one")
				.intValue());
	}
}
