package gubo.doc;

import gubo.http.grizzly.handlergenerator.MappingToPath;
import gubo.http.querystring.QueryStringBinder;
import gubo.http.querystring.QueryStringField;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 假定文档要描述的主体是 HTTP API，来生成文档，会生成curl命令的示例。 和{@link Comment}配合。
 * 
 **/
public class HttpApiDocGenerator {

	/**
	 * 为 interfc 上所有 同时被 {@link Comment} 和 {@link MappingToPath} 标注的 method
	 * 生成文档。
	 * 
	 * @throws Exception
	 * 
	 **/
	public String generateDoc(Class<?> interfc, String urlPrefix)
			throws Exception {
		Method[] methods = interfc.getMethods();

		List<String> doc = new LinkedList<String>();
		for (Method m : methods) {
			String docForMethhod = this.generateDoc(m, urlPrefix);
			doc.add(docForMethhod);
			System.out.println(docForMethhod);
		}
		return "";
	}

	Object createForExample(Class<?> clazz) throws Exception {
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

	StringBuilder generateDocForFields(Class<?> parameterType) {

		StringBuilder sb = new StringBuilder();
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
			String queryStrfieldName = f.getName();
			if (anno != null) {
				if (anno.name() != null && anno.name().length() > 0) {
					queryStrfieldName = anno.name();
				}
			}

			sb.append(queryStrfieldName + ": " + fieldComment.value());
			sb.append("\n");
		}
		return sb;
	}

	public String generateDoc(Method method, String urlPrefix) throws Exception {
		Comment comment = method.getAnnotation(Comment.class);
		MappingToPath mappingToPath = method.getAnnotation(MappingToPath.class);
		if (comment == null) {
			return "";
		}
		if (mappingToPath == null) {
			return "";
		}

		String url = urlPrefix + mappingToPath.value();
		String httpMethod = mappingToPath.method().toUpperCase();
		StringBuilder sb = new StringBuilder();
		sb.append(url);
		sb.append("\n");
		sb.append("method: " + httpMethod);
		sb.append("\n");
		sb.append(comment.value());
		sb.append("\n");

		Class<?> parameterType = method.getParameters()[0].getType();
		sb.append("请求参数说明: \n");
		sb.append(generateDocForFields(parameterType));

		String postData = "";
		String querystring = "";

		Object parameterObj = createForExample(parameterType);

		QueryStringBinder binder = new QueryStringBinder();
		String querystringKVpairs = binder.toQueryString(parameterObj, null);

		if ("POST".equals(httpMethod)) {
			postData = " -H'Content-Type: application/x-www-form-urlencoded' -d'"
					+ querystringKVpairs + "' ";

		} else if ("GET".equals(httpMethod)) {
			querystring = "?" + querystringKVpairs;
		}

		String curl = "curl -X" + httpMethod + postData + " '" + url
				+ querystring + "'";

		sb.append("curl示例: " + curl);
		sb.append("\n");

		Class<?> returnType = method.getReturnType();
		sb.append("回复数据字段说明: \n");
		sb.append(this.generateDocForFields(returnType));
		Object returnObj = createForExample(returnType);

		ObjectMapper mapper = new ObjectMapper();
		sb.append("回复数据示例: \n");
		sb.append(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
				returnObj));

		return sb.toString();
	}
}
