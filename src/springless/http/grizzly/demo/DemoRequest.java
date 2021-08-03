package springless.http.grizzly.demo;

import springless.http.querystring.MultipartFile;
import springless.http.querystring.QueryStringField;

public class DemoRequest {

	@QueryStringField
	public String owner = "";
	
	// 取 一个叫做 qualification 的multipart 字段。
	@QueryStringField(required = false)
	public MultipartFile qualification;
}
