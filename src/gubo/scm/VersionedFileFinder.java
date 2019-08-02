package gubo.scm;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 从指定路径找到版本最新的文件。
// 不找子目录。
public class VersionedFileFinder {

	/**
	 * 
	 * @param filename
	 *            pattern containing {version} e.g.
	 *            interested-file.{version}.txt
	 **/
	public static VersionedFile findLatest(String dir, String pattern) {
		List<VersionedFile> vfiles = findMatchingFiles(dir, pattern);
		if (vfiles.isEmpty()) {
			return null;
		}
		
		Collections.sort(vfiles);
		Collections.reverse(vfiles);
		return vfiles.get(0);
	}
	
	public static List<VersionedFile> findMatchingFiles(String dir, String pattern) {
		String reg = pattern.replace("{version}", "(\\d+\\.\\d+\\.\\d+)");
		Pattern regex = Pattern.compile(reg);

		File folder = new File(dir);
		LinkedList<VersionedFile> filenames = new LinkedList<VersionedFile>();
		for (final File f : folder.listFiles()) {

			if (f.isDirectory()) {
				continue;
			}

			if (f.isFile()) {
				if (f.getName().matches(reg)) {
					Matcher m = regex.matcher(f.getName());
					m.find();
					ThreeDigitsVersion version = new ThreeDigitsVersion(m.group(1));
					VersionedFile vf = new VersionedFile();
					vf.filename = f.getName();
					vf.version = version;
					filenames.add(vf);
				}
			}
		}
		return filenames;
	}
}
