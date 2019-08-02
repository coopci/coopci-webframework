package gubo.scm;

public class VersionedFile implements Comparable<VersionedFile>{
	public ThreeDigitsVersion version;
	public String filename;
	@Override
	public int compareTo(VersionedFile other) {
		return this.version.compareTo(other.version);
	}
}
