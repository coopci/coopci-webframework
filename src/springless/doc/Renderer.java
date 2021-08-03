package springless.doc;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import springless.doc.ApiDocument.ParameterDocument;

public class Renderer {

	String render(List<ApiDocument> apiDocuments) throws Exception {

		StringBuilder sb = new StringBuilder();

		for (ApiDocument doc : apiDocuments) {
			sb.append(render(doc));

			sb.append("\n\n");
		}
		return sb.toString();
	}

	String render(ApiDocument apiDocument) throws JsonProcessingException {
		StringBuilder sb = new StringBuilder();
		sb.append(apiDocument.url);
		sb.append("\n");
		sb.append("method: " + apiDocument.httpMethod);
		sb.append("\n");
		sb.append(apiDocument.desc);
		sb.append("\n");

		sb.append("请求参数说明: \n");
		for (ParameterDocument pd : apiDocument.parameterDocuments) {
			sb.append(pd.name + ": " + pd.desc);
			sb.append("\n");
		}
		sb.append("curl请求示例: " + apiDocument.curl);
		sb.append("\n");

		sb.append("回复数据字段说明: \n");
		for (ParameterDocument pd : apiDocument.responseDocuments) {
			sb.append(pd.name + ": " + pd.desc);
			sb.append("\n");
		}

		ObjectMapper mapper = new ObjectMapper();
		sb.append("回复数据示例: \n");
		sb.append(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
				apiDocument.responseExample));

		return sb.toString();
	}
}
