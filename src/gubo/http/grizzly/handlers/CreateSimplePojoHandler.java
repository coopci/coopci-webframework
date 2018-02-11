package gubo.http.grizzly.handlers;

import java.util.HashMap;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import api.handlers.BaseApiHander;
import gubo.db.ISimplePoJo;

public class CreateSimplePojoHandler extends BaseApiHander {
	
	Class<? extends ISimplePoJo> clazz;
	public CreateSimplePojoHandler(Class<? extends ISimplePoJo> clazz) {
		this.clazz = clazz;
	}
	

	@Override
	public Object doPost(Request request, Response response) throws Exception {
		this.authCheck(request);
		HashMap<String, Object> ret = createSimplePojo(request, clazz);
		return ret;
	}

}
