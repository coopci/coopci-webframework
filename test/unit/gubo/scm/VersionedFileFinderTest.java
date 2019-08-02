package gubo.scm;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class VersionedFileFinderTest {

	@Test
	public void testFindMatchingFiles() {
		
		List<VersionedFile> files = VersionedFileFinder.findMatchingFiles("versioned-files", "interested-file.{version}.txt");
		for(VersionedFile vfile : files) {
			System.out.println(vfile.version);
			System.out.println(vfile.filename);
		}
	}
	
	@Test
	public void testFindLatest() {

		VersionedFile vfile = VersionedFileFinder.findLatest("versioned-files", "interested-file.{version}.txt");
		assertEquals("interested-file.2.0.1.txt", vfile.filename);
	}
}
