package gubo.http.grizzly.handlers;

import gubo.db.IConnectionProvider;
import gubo.db.ISimplePoJo;

import java.util.HashMap;

public class EnableByIdHandler extends UpdateByIdHandler {
	public final static HashMap<String, String> enabled = new HashMap<String, String>();
	static {
		enabled.put("enabled", "true");
	}
	
	public EnableByIdHandler(Class<? extends ISimplePoJo> clazz, IConnectionProvider connectionProvider) {
		super(clazz, connectionProvider, new String[0], enabled);
	}
}
