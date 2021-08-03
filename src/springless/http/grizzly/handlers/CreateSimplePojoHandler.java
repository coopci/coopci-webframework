package springless.http.grizzly.handlers;

import springless.db.ISimplePoJo;
import springless.http.grizzly.NannyHttpHandler;

import java.util.HashMap;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

public class CreateSimplePojoHandler extends NannyHttpHandler {
	
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
