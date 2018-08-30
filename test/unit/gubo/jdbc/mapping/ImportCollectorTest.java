package gubo.jdbc.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ImportCollectorTest {

	@Test
	public void testScan() {

		ImportCollector collector = new ImportCollector();
		assertEquals(0, collector.getImports().size());
		collector.scan(" import  adfa.dfgfdsg ;");
		collector.scan("import  static adfa.dfgfdsg ;");
		collector.scan("imports  adfa.dfgfdsg;");
		assertEquals(2, collector.getImports().size());
		assertTrue(collector.getImports().contains("adfa.dfgfdsg"));
		assertTrue(collector.getImports().contains("static adfa.dfgfdsg"));
	}

	@Test
	public void testMerge() {

		ImportCollector collector1 = new ImportCollector();
		collector1.scan(" import  adfa.dfgfdsg ;");
		collector1.scan("import  static adfa.dfgfdsg ;");
		collector1.scan("imports  adfa.dfgfdsg;");

		ImportCollector collector2 = new ImportCollector();
		collector2.scan(" import  adfa2.dfgfdsg ;");
		collector2.scan("import  static adfa2.dfgfdsg ;");
		collector2.scan("imports  adfa2.dfgfdsg;");

		String imports = collector1.merge(collector2);

		assertEquals(4, imports.split("\n").length);

		assertTrue(imports.indexOf("import adfa.dfgfdsg;") >= 0);
		assertTrue(imports.indexOf("import static adfa.dfgfdsg;") >= 0);
		assertTrue(imports.indexOf("import adfa2.dfgfdsg;") >= 0);
		assertTrue(imports.indexOf("import static adfa2.dfgfdsg;") >= 0);
	}

}
