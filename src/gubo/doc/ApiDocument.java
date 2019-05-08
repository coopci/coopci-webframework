package gubo.doc;

import gubo.http.grizzly.handlergenerator.MappingToPath;
import gubo.http.grizzly.handlers.InMemoryMultipartEntryHandler;
import gubo.http.querystring.QueryStringBinder;
import gubo.http.querystring.QueryStringField;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

/**
 * 一个http API的文档。
 **/
public class ApiDocument {

	public static class ParameterDocument {
		public String name;
		public String desc;
	}

	List<ParameterDocument> parameterDocuments;
	List<ParameterDocument> responseDocuments;
	HashMap<String, String> parameterExample;
	Object requestExample;
	Object responseExample;
	String httpMethod;
	String desc;
	String curl;
	String url;

	/**
	 * {@link Comment} 的group属性。
	 **/
	String group;
	
	/**
     * {@link Comment} 的name属性。
     **/
	String name;
	boolean deprecated = false;
	String deprecatedBy = "";
	String f() {
		return "dsf";
	}

	String responseExampleJson() throws JsonProcessingException {
		ObjectMapper om = new ObjectMapper();

		String ret = om.writerWithDefaultPrettyPrinter().writeValueAsString(
				responseExample);
		return ret;
	}

	public static Object createForExample(Class<?> clazz) throws Exception {
		Object parameterObj = clazz.newInstance();

		Field[] fields = FieldUtils.getAllFields(clazz);
		HashMap<String, String> data = new HashMap<String, String>();
		for (Field f : fields) {
			Comment comment = f.getAnnotation(Comment.class);
			if (comment == null) {
				continue;
			}

			QueryStringField anno = f.getAnnotation(QueryStringField.class);
			if (anno == null) {
				continue;
			}
			String queryStrfieldName = f.getName();
			if (anno != null) {
				if (anno.name() != null && anno.name().length() > 0) {
					queryStrfieldName = anno.name();
				}
			}

			data.put(queryStrfieldName, comment.example());
		}

		QueryStringBinder binder = new QueryStringBinder();
		binder.bind(data, parameterObj);

		return parameterObj;
	}

	public static List<ParameterDocument> buildParameterDocuments(
			Class<?> parameterType) {
		List<ParameterDocument> ret = new LinkedList<ParameterDocument>();
		Field[] fields = FieldUtils.getAllFields(parameterType);
		for (Field f : fields) {

			Comment fieldComment = f.getAnnotation(Comment.class);
			if (fieldComment == null) {
				continue;
			}

			QueryStringField anno = f.getAnnotation(QueryStringField.class);
			if (anno == null) {
				continue;
			}
			ParameterDocument pd = new ParameterDocument();
			String queryStrfieldName = f.getName();
			if (anno != null) {
				if (anno.name() != null && anno.name().length() > 0) {
					queryStrfieldName = anno.name();
				}
			}
			pd.name = queryStrfieldName;
			pd.desc = fieldComment.value();
			ret.add(pd);
		}
		return ret;
	}

	// 选择grizzly的request 和 response以外的参数。
	static Class<?> getParameterClass(Method method) {
		for (Class<?> c : method.getParameterTypes()) {
			if (c.equals(Request.class)) {
				continue;
			} else if (c.equals(Response.class)) {
				continue;
			} else if (c.equals(InMemoryMultipartEntryHandler.class)) {
				continue;
			} else {
				return c;
			}
		}
		return null;
	}
		
	public static ApiDocument build(Method method, String urlPrefix)
			throws Exception {
		ApiDocument ret = new ApiDocument();

		Comment comment = method.getAnnotation(Comment.class);
		MappingToPath mappingToPath = method.getAnnotation(MappingToPath.class);
		if (comment == null) {
			return null;
		}
		if (mappingToPath == null) {
			return null;
		}
		if (!Strings.isNullOrEmpty(comment.deprecatedBy())) {
			ret.deprecated = true;
			ret.deprecatedBy = comment.deprecatedBy();
					
		}
		
		ret.url = urlPrefix + mappingToPath.value();
		ret.httpMethod = mappingToPath.method().toUpperCase();
		ret.desc = comment.value();
		ret.name = comment.name();
		ret.group = comment.group();
		Class<?> parameterType = getParameterClass(method);
		ret.parameterDocuments = buildParameterDocuments(parameterType);

		String postData = "";
		String querystring = "";

		ret.requestExample = createForExample(parameterType);

		QueryStringBinder binder = new QueryStringBinder();
		ret.parameterExample = binder.toHashMap(ret.requestExample,
				null);
		String querystringKVpairs = binder.toQueryString(ret.requestExample,
				null);

		if ("POST".equals(ret.httpMethod)) {
			postData = " -H'Content-Type: application/x-www-form-urlencoded' -d'"
					+ querystringKVpairs + "' ";

		} else if ("GET".equals(ret.httpMethod)) {
			querystring = "?" + querystringKVpairs;
		}

		ret.curl = "curl -X" + ret.httpMethod + postData + " '" + ret.url
				+ querystring + "'";

		// 下面是回复的文档

		Class<?> returnType = method.getReturnType();
		ret.responseDocuments = buildParameterDocuments(returnType);

		ret.responseExample = createForExample(returnType);
		return ret;
	}

}
