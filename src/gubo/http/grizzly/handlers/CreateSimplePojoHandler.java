package gubo.http.grizzly.handlers;

import gubo.db.ISimplePoJo;
import gubo.http.grizzly.ApiHttpHandler;

import java.util.HashMap;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

public class CreateSimplePojoHandler extends ApiHttpHandler {
	
	private Class<? extends ISimplePoJo> clazz;
	public Class<? extends ISimplePoJo> getClazz() {
		return clazz;
	}


	public void setClazz(Class<? extends ISimplePoJo> clazz) {
		this.clazz = clazz;
	}


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
