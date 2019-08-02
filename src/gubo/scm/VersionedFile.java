package gubo.scm;

public class VersionedFile implements Comparable<VersionedFile>{
	ThreeDigitsVersion version;
	String filename;
	@Override
	public int compareTo(VersionedFile other) {
		return this.version.compareTo(other.version);
	}
}
