package gubo.jdbc.mapping;

import java.util.Set;
import java.util.TreeSet;

import com.google.common.collect.Sets;

public class ImportCollector {
	TreeSet<String> imports = new TreeSet<String>();

	public TreeSet<String> getImports() {
		return imports;
	}

	public void scan(String code) {
		for (String line : code.split("\n")) {
			line = line.trim();
			if (line.startsWith("import ")) {
				String tail = line.substring("import ".length());
				String imported = tail.replace(";", "").trim();
				this.imports.add(imported);
			}
		}
	}

	public String merge(ImportCollector other) {
		// Set<String>
		Set<String> union = Sets.union(this.getImports(), other.getImports());
		StringBuilder sb = new StringBuilder();
		for (String imported : union) {
			sb.append("import ");
			sb.append(imported);
			sb.append(";\r\n");
		}

		return sb.toString();
	}
}
