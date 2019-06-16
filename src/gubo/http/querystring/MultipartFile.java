package gubo.http.querystring;


public class MultipartFile {

	final private byte[] bytes;
	final private String filename;
	

	public MultipartFile(String filename, byte[] input) {
		this.filename = filename;
		this.bytes = input;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public String getFilename() {
		return filename;
	}
	
}
