package gubo.doc;

import gubo.http.grizzly.handlergenerator.MappingToPath;
import gubo.http.querystring.QueryStringBinder;
import gubo.http.querystring.QueryStringField;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 假定文档要描述的主体是 HTTP API，来生成文档，会生成curl命令的示例。 和{@link Comment}配合。
 * 
 **/
public class HttpApiDocGenerator {
	public static Logger logger = LoggerFactory
			.getLogger(HttpApiDocGenerator.class);

	/**
	 * 为 interfc 上所有 同时被 {@link Comment} 和 {@link MappingToPath} 标注的 method
	 * 生成文档。
	 * 
	 * @throws Exception
	 * 
	 **/
	public List<ApiDocument> generateDoc(Class<?> interfc, String urlPrefix)
			throws Exception {
		Method[] methods = interfc.getMethods();

		List<ApiDocument> docs = new LinkedList<ApiDocument>();
		for (Method m : methods) {
			logger.warn("Try method: {}", m);
			try {
				ApiDocument apiDocument = this.generateDoc(m, urlPrefix);
				if (apiDocument == null) {
					logger.warn("Ignoring method: {}", m);
					continue;
				}
				docs.add(apiDocument);
			} catch (Exception ex) {
				logger.error("" + m.toString(), ex);
				throw ex;
			}
		}

		return docs;
	}
	
	public String renderDocument(List<ApiDocument> docs)
			throws Exception {

		JTwigRenderer r = new JTwigRenderer();

		String ret = r.render(docs);

		OutputStream os = new FileOutputStream(new File("documents.html"));
		os.write(ret.getBytes());
		os.close();
		System.out.println(ret);
		return ret;
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

	public ApiDocument generateDoc(Method method, String urlPrefix)
			throws Exception {
		ApiDocument apiDocument = ApiDocument.build(method, urlPrefix);
		if (apiDocument == null) {
			return null;
		}
		return apiDocument;
	}
}
