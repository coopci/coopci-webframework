package gubo.http.grizzly.demo;

import gubo.http.querystring.MultipartFile;
import gubo.http.querystring.QueryStringField;

public class DemoRequest {

	@QueryStringField
	public String owner = "";
	
	// 取 一个叫做 qualification 的multipart 字段。
	@QueryStringField(required = false)
	public MultipartFile qualification;
}
