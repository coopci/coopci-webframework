package springless.http.grizzly.handlers;

import java.util.HashMap;

import springless.db.ISimplePoJo;

public class DisableByIdHandler extends UpdateByIdHandler {
	public final static HashMap<String, String> disabled = new HashMap<String, String>();
	static {
		disabled.put("enabled", "false");
	}
	
	public DisableByIdHandler(Class<? extends ISimplePoJo> clazz) {
		super(clazz, new String[0], disabled);
	}
}
