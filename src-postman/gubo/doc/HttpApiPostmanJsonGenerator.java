package gubo.doc;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.ObjectMapper;

import gubo.doc.ApiDocument.ParameterDocument;
import gubo.http.querystring.QueryStringBinder;
import gubo.postman.Collection;
import gubo.postman.Item;
import gubo.postman.Request;
import gubo.postman.Request.Body.Urlencoded;

/**
 * 给出java接口，用以生成Postman collection格式的json文件。 暂时未实现建立ItemGroup进行分类。
 * 根据“https://www.postmanlabs.com/postman-collection/tutorial-concepts.html”
 * 文档的示例构建数据结构。
 *
 */
public class HttpApiPostmanJsonGenerator {
	public static Logger logger = LoggerFactory.getLogger(HttpApiPostmanJsonGenerator.class);

	/**
	 * 生成postman collection 格式的json文件。
	 * @param docs 所有http API的文档
	 * @param fileName 生成的json文件使用的名字
	 * @param collectionName collection使用的名字
	 * @throws Exception
	 */
	public void generateCollectionJson(List<ApiDocument> docs, String fileName, String collectionName) throws Exception {

		List<Item> itemList = new LinkedList<Item>();
		Item item = new Item();
		try {
			for (ApiDocument doc : docs) {
				if (doc.deprecated) {
					continue;
				}
				item = this.buildItem(doc);
				if (item == null) {
					logger.warn("Ignoring doc: {}", doc);
					continue;
				}
				itemList.add(item);
			}
		} catch (Exception e) {
			logger.error("Generate collection failed, ", e);
		}

		Collection collection = this.buildCollection(collectionName);
		collection.item = itemList;

		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(collection);
		File file = new File(fileName + ".json");
		JsonFactory jfactory = new JsonFactory();
		JsonGenerator jGenerator = jfactory.createGenerator(file, JsonEncoding.UTF8);
		SerializedString rawString = new SerializedString(jsonStr);
		jGenerator.writeRaw(rawString);
	}

	public Collection buildCollection(String fileName) {
		Collection collection = new Collection();
		Collection.Information info = new Collection.Information();

		info._postman_id = UUID.randomUUID().toString();
		info.name = fileName;
		info.schema = "https://schema.getpostman.com/json/collection/v2.0.0/collection.json";

		collection.info = info;
		return collection;
	}

	/**
	 * 构建每一个请求条目。
	 * @param doc
	 * @return
	 * @throws Exception
	 */
	public Item buildItem(ApiDocument doc) throws Exception {
		Request request = new Request();
		Item item = new Item();
		if (doc.httpMethod.equals("POST")) {
			Request.Header header = new Request.Header();
			Request.Body body = new Request.Body();
			List<Request.Header> headerList = new LinkedList<Request.Header>();

			// 构建请求中的header。
			header.key = "Content-Type";
			header.value = "application/x-www-form-urlencoded";
			headerList.add(header);

			// 构建请求中的body。
			body.mode = "urlencoded";
			body.urlencoded = this.buildParameter(doc.parameterDocuments, doc.parameterExample);

			request.url = doc.url;
			request.method = doc.httpMethod;
			request.description = doc.desc;
			request.header = headerList;
			request.body = body;
		} else if (doc.httpMethod.equals("GET")) {
			QueryStringBinder binder = new QueryStringBinder();
			String parameter = binder.toQueryString(doc.parameterExample);
			request.url = doc.url + "?" + parameter;
			request.method = doc.httpMethod;
			request.description = doc.desc;
		} else {
			logger.warn("Failed generate this method: {}", doc.httpMethod);
			request.url = doc.url;
			request.method = doc.httpMethod;
			request.description = doc.desc;
		}
		item.name = doc.url;
		item.request = request;

		return item;
	}

	/**
	 * 构建每一个请求用到的参数。
	 * @param fields
	 * @param fieldsMap
	 * @return
	 */
	public List<Urlencoded> buildParameter(List<ParameterDocument> fields, HashMap<String, String> fieldsMap) {
		List<Urlencoded> urlencodedList = new LinkedList<Urlencoded>();

		for (ParameterDocument f : fields) {
			Urlencoded encoded = new Urlencoded();
			encoded.key = f.name;
			encoded.value = fieldsMap.get(f.name);
			encoded.description = f.desc;
			urlencodedList.add(encoded);
		}

		return urlencodedList;
	}

}
