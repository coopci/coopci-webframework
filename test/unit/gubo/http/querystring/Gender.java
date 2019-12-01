package gubo.http.querystring;

public enum Gender {
	Male("男"),
	Female("女");
	
	public final String chinese;
	Gender(String chinese) {
		this.chinese = chinese;
	}
	
}
