package gubo.http.grizzly;

import java.util.HashMap;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import api.handlers.BaseApiHander;
import gubo.scm.Version;

// 报告当前程序的代码版本
public class VersionHandler extends BaseApiHander {

	@Override
	public Object doGet(Request request, Response response) throws Exception {
		HashMap<String, Object> ret = this.getOKResponse();

		HashMap<String, String> data = new HashMap<String, String>();
		data.put("svnVersion", Version.svnVersion);
		data.put("gitVersion", Version.gitVersion);

		ret.put("data", data);
		return ret;
	}

}
