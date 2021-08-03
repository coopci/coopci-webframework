package springless.http.querystring;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Gender {
	Male("男"),
	Female("女");
	
	public final String chinese;
	Gender(String chinese) {
		this.chinese = chinese;
	}
	
}
