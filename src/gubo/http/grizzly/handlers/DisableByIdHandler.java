package gubo.http.grizzly.handlers;

import gubo.db.ISimplePoJo;

import java.util.HashMap;

public class DisableByIdHandler extends UpdateByIdHandler {
	public final static HashMap<String, String> disabled = new HashMap<String, String>();
	static {
		disabled.put("enabled", "false");
	}
	
	public DisableByIdHandler(Class<? extends ISimplePoJo> clazz) {
		super(clazz, new String[0], disabled);
	}
}
