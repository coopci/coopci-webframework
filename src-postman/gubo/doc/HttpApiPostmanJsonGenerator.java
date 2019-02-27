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
import com.google.common.base.Strings;

import gubo.doc.ApiDocument.ParameterDocument;
import gubo.http.querystring.QueryStringBinder;
import gubo.postman.Collection;
import gubo.postman.Item;
import gubo.postman.ItemGroup;
import gubo.postman.Request;
import gubo.postman.Request.Body.Urlencoded;

/**
 * 给出java接口，用以生成Postman collection格式的json文件。
 * 根据“https://www.postmanlabs.com/postman-collection/tutorial-concepts.html”
 * 文档的示例构建数据结构。
 *
 */
public class HttpApiPostmanJsonGenerator {
	public static Logger logger = LoggerFactory.getLogger(HttpApiPostmanJsonGenerator.class);
	private static List<ItemGroup> cachedItemGroupList = new LinkedList<ItemGroup>();
	private static ItemGroup cachedItemGroup = new ItemGroup();
	private static HashMap<String, ItemGroup> cachedItemGroupMap = new HashMap<String, ItemGroup>();

	/**
	 * 生成postman collection 格式的json文件。
	 * 
	 * @param docs
	 *            所有http API的文档。
	 * @param filePath
	 *            文件路径,只写文件名则表示建在当前项目根目录下。
	 * @param collectionName
	 *            collection使用的名字。
	 * @throws Exception
	 */
	public void generateCollectionJson(List<ApiDocument> docs, String filePath, String collectionName)
			throws Exception {

		List<Object> itemList = new LinkedList<Object>();
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
				if (!Strings.isNullOrEmpty(doc.group)) {
					itemList.add(cachedItemGroup);
				}
				itemList.add(item);
				cachedItemGroupList.clear();
				cachedItemGroup = new ItemGroup();
			}
		} catch (Exception e) {
			logger.error("Generate collection failed, ", e);
		}

		Collection collection = this.buildCollection(collectionName);
		collection.item = itemList;

		try {
			ObjectMapper mapper = new ObjectMapper();
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(collection);
			File file = new File(filePath);
			JsonFactory jfactory = new JsonFactory();
			JsonGenerator jGenerator = jfactory.createGenerator(file, JsonEncoding.UTF8);
			SerializedString rawString = new SerializedString(jsonStr);
			jGenerator.writeRaw(rawString);
		} catch (Exception e) {
			logger.error("Failed write json file: ", e);
		}

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
	 * 
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
		if (!Strings.isNullOrEmpty(doc.group)) {
			if (cachedItemGroupMap.containsKey(doc.group)) {
				this.addItem(doc, item);
			}
			this.buildItemGroup(doc, item);
		}
		return item;
	}

	/**
	 * 构建层级结构。
	 * 
	 * @param doc
	 * @param item
	 */
	private void buildItemGroup(ApiDocument doc, Item item) {

		String[] strList = doc.group.split("/");
		for (int i = 0; i < strList.length; i++) {
			ItemGroup ig = new ItemGroup();
			ig.name = strList[i];
			if (i >= 1) {
				ig._postman_isSubFolder = true;
			}
			cachedItemGroupList.add(ig);
		}
		ItemGroup itemGroup = new ItemGroup();

		LinkedList<Object> tempList = new LinkedList<Object>();
		if (cachedItemGroupList.size() == 1) {
			itemGroup = cachedItemGroupList.get(0);
			tempList.add(item);
			itemGroup.item = tempList;
		} else {
			for (int i = cachedItemGroupList.size() - 1; i > 0; i--) {
				if (i == cachedItemGroupList.size() - 1) {
					ItemGroup ig = cachedItemGroupList.get(i);
					tempList.add(item);
					ig.item = tempList;
				}
				tempList.add(cachedItemGroupList.get(i - 1));
				cachedItemGroupList.get(i - 1).item = tempList;
				itemGroup = cachedItemGroupList.get(i - 1);
			}
		}
		cachedItemGroupMap.put(doc.group, itemGroup);
	}

	/**
	 * 向已存在的文件夹下添加item。
	 * 
	 * @param doc
	 * @param item
	 */
	private void addItem(ApiDocument doc, Item item) {
		ItemGroup itemGroup = cachedItemGroupMap.get(doc.group);
		String[] strList = doc.group.split("/");
		if (strList.length == 1) {
			itemGroup.item.add(item);
		} else {
			for (int i = 0; i < strList.length - 1; i++) {
				cachedItemGroup = (ItemGroup) itemGroup.item.get(0);
			}
			cachedItemGroup.item.add(item);
		}

	}

	/**
	 * 构建每一个请求用到的参数。
	 * 
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
