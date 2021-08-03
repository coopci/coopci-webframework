package springless.http.grizzly.handlers;

import java.util.HashMap;

import springless.db.ISimplePoJo;

public class EnableByIdHandler extends UpdateByIdHandler {
	public final static HashMap<String, String> enabled = new HashMap<String, String>();
	static {
		enabled.put("enabled", "true");
	}
	
	public EnableByIdHandler(Class<? extends ISimplePoJo> clazz) {
		super(clazz, new String[0], enabled);
	}
}
