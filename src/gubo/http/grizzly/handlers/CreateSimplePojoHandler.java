package gubo.http.grizzly.handlers;

import gubo.db.IConnectionProvider;
import gubo.db.ISimplePoJo;
import gubo.http.grizzly.ApiHttpHandler;

import java.util.HashMap;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

public class CreateSimplePojoHandler extends ApiHttpHandler {
	
	Class<? extends ISimplePoJo> clazz;
	public CreateSimplePojoHandler(Class<? extends ISimplePoJo> clazz, IConnectionProvider connectionProvider) {
		this.clazz = clazz;
		this.setConnectionProvider(connectionProvider);
	}
	

	@Override
	public Object doPost(Request request, Response response) throws Exception {
		this.authCheck(request);
		HashMap<String, Object> ret = createSimplePojo(request, clazz);
		return ret;
	}

}
