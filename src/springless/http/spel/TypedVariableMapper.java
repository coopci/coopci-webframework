package springless.http.spel;

import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.glassfish.grizzly.http.server.Request;

/**
 * 把http请求的参数转成 变量名=>java对象 的map
 * http请求可以指定变量的字符串值和类型，例如:
 * 
 * foo__value=1000&foo__type=BigDecimal
 * 
 * 表示变量foo的字串值是1000，类型是BigDecimal。
 * BigDecimal类型会被转成java.math.BigDecimal。
 * 
 *  
 * */
public class TypedVariableMapper {

	public Map<String, Object> map(Request request) {
		Map<String, Object> ret = new HashMap<String, Object>();
		
		
		for (String pn: request.getParameterNames()) {
			if (pn.endsWith("__value")) {
				String vname = pn.substring(0, pn.length() - "__value".length());
				ret.put(vname, request.getParameter(pn));
			}
		}
		
		for (String vn : ret.keySet()) {
			String pn = vn+"__type";
			String type = request.getParameter(pn);
			if (type == null) {
				// 不指定类型就当字符串
				continue;
			}
			if ("String".equals(type)) {
				continue;
			}
			if ("BigDecimal".equals(type)) {
				String svalue = (String)ret.get(vn);
				BigDecimal value = new BigDecimal(svalue);
				ret.put(vn, value);
			}
			
		}
		return ret;
	}
	
}
