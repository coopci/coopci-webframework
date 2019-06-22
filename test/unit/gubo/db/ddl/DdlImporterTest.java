package gubo.db.ddl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

public class DdlImporterTest {
	@Test
	public void testLoadDdlList() throws IOException {
		DdlImporter testee = new DdlImporter();
		List<String> ddlList = testee.loadDdlList("test/unit/gubo/db/ddl/sample-ddl.sql");
		
		assertEquals(2, ddlList.size());
		
		assertTrue(ddlList.get(0).startsWith("CREATE TABLE `user` ("));
		assertTrue(ddlList.get(1).startsWith("create or replace view v_user as"));
	}
}
