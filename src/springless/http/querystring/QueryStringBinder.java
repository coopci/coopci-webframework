package springless.http.querystring;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.glassfish.grizzly.http.multipart.ContentDisposition;
import org.glassfish.grizzly.http.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.UrlEscapers;

import springless.exceptions.QueryStringParseException;
import springless.exceptions.RequiredParametersMissingException;
import springless.http.grizzly.handlers.InMemoryMultipartEntryHandler;
import springless.http.querystring.parsers.BigDecimalParser;
import springless.http.querystring.parsers.BooleanFieldParser;
import springless.http.querystring.parsers.DatetimeParser;
import springless.http.querystring.parsers.DoubleFieldParser;
import springless.http.querystring.parsers.EnumFieldParser;
import springless.http.querystring.parsers.FloatFieldParser;
import springless.http.querystring.parsers.IntegerFieldParser;
import springless.http.querystring.parsers.LongFieldParser;
import springless.http.querystring.parsers.NullParser;
import springless.http.querystring.parsers.StringFieldParser;
import springless.http.querystring.parsers.TimeParser;
import springless.http.querystring.parsers.TimestampParser;


// TODO 处理 required=true的MultipartFile类型的字段。
/**
 * 把http query string 指定的参数bind到pojo的 @QueryStringField 的字段上。
 * jdbc相关的是遗留功能，以后会被取代。
 **/
public class QueryStringBinder {
	public static Logger logger = LoggerFactory
			.getLogger(QueryStringBinder.class);

	public static class Binding {
		Class<?> clazz;

		// key 是 querystring中的字段名，不是Field的名字。
		ConcurrentHashMap<String, IQueryStringFieldParser> _cachedParses = new ConcurrentHashMap<String, IQueryStringFieldParser>();
		ConcurrentHashMap<String, Field> _cachedMultiparFileFields = new ConcurrentHashMap<String, Field>();

		public Field getMultiparFileField(String fieldname) {
			return _cachedMultiparFileFields.get(fieldname);
		}
		public IQueryStringFieldParser getParser(String fieldname) {
			return this._cachedParses.get(fieldname);

		}

		HashSet<Field> requiredFields = new HashSet<Field>();

		/**
		 * 复制一份requiredFields。
		 */
		public HashSet<Field> getRequiredFieldsChecking() {
			@SuppressWarnings("unchecked")
			HashSet<Field> ret = (HashSet<Field>) requiredFields.clone();
			return ret;
		}

		public void tryAddField(Field f) throws InstantiationException,
				IllegalAccessException {
			if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
				return;
			}

			QueryStringField anno = f.getAnnotation(QueryStringField.class);

			if (anno == null) {
				// System.out.println("anno == null: " + f.getName());

				logger.debug("anno == null, class: {}, field name: {} ",
						clazz.getName(), f.getName());
				return;
			}

			String queryStrfieldName = f.getName();

			if (anno != null) {
				if (anno.name() != null && anno.name().length() > 0) {
					queryStrfieldName = anno.name();
				}
				if (anno.required()) {
					this.requiredFields.add(f);
				}

			}

			// 如果字段是MultipartFile类型，则特殊处理。
			if (f.getType() == MultipartFile.class) {
				this._cachedMultiparFileFields.put(queryStrfieldName, f);
				return;
			}
			
			// System.out.println("queryStrfieldName = " + queryStrfieldName);

			Class<? extends IQueryStringFieldParser> deserializerClass = anno
					.deserializer();
			if (deserializerClass == NullParser.class) {
				if (f.getType().isEnum()) {
					deserializerClass = EnumFieldParser.class;
				} else if (f.getType() == String.class) {
					deserializerClass = StringFieldParser.class;
				} else if (f.getType() == Long.class) {
					deserializerClass = LongFieldParser.class;
				} else if (f.getType() == long.class) {
					deserializerClass = LongFieldParser.class;
				} else if (f.getType() == Integer.class) {
					deserializerClass = IntegerFieldParser.class;
				} else if (f.getType() == int.class) {
					deserializerClass = IntegerFieldParser.class;
				} else if (f.getType() == Boolean.class) {
					deserializerClass = BooleanFieldParser.class;
				} else if (f.getType() == boolean.class) {
					deserializerClass = BooleanFieldParser.class;
				} else if (f.getType() == Float.class) {
					deserializerClass = FloatFieldParser.class;
				} else if (f.getType() == float.class) {
					deserializerClass = FloatFieldParser.class;
				} else if (f.getType() == Double.class) {
					deserializerClass = DoubleFieldParser.class;
				} else if (f.getType() == double.class) {
					deserializerClass = DoubleFieldParser.class;
				} else if (f.getType().isAssignableFrom(Date.class)) {
					deserializerClass = DatetimeParser.class;
				} else if (f.getType() == Time.class) {
					deserializerClass = TimeParser.class;
				} else if (f.getType() == BigDecimal.class) {
					deserializerClass = BigDecimalParser.class;
				} else if (f.getType() == Timestamp.class) {
					deserializerClass = TimestampParser.class;
				}
			}

			// System.out.println("deserializerClass = " + deserializerClass);
			IQueryStringFieldParser deserializerObj = null;
			if (deserializerClass == EnumFieldParser.class) {
				deserializerObj = new EnumFieldParser((Class<? extends Enum>)f.getType());
			} else {
				deserializerObj = deserializerClass
					.newInstance();
			}
			
			if (anno != null) {
				deserializerObj.setIgnoreMalFormat(anno.ignoreMalFormat());
				deserializerObj.setCanBeBlank(anno.canBeBlank());
				deserializerObj.setDoTrim(anno.doTrim());
			}

			deserializerObj.setField(f);
			this._cachedParses.put(queryStrfieldName, deserializerObj);

		}
	}

	public Binding constructBinding(Class<? extends Object> clazz)
			throws InstantiationException, IllegalAccessException {
		Binding binding = new Binding();
		binding.clazz = clazz;

		Field[] fields = FieldUtils.getAllFields(clazz);
		for (Field f : fields) {
			binding.tryAddField(f);
		}

		return binding;
	}

	public Binding constructBinding(Object pojo) throws InstantiationException,
			IllegalAccessException {
		return constructBinding(pojo.getClass());
	}

	static ConcurrentHashMap<Class<?>, Binding> _cachedBindings = new ConcurrentHashMap<Class<?>, Binding>();

	public Binding getBinding(Class<? extends Object> clazz)
			throws InstantiationException, IllegalAccessException {
		Binding ret = _cachedBindings.get(clazz);
		if (ret != null) {
			return ret;
		}
		ret = constructBinding(clazz);
		_cachedBindings.put(clazz, ret);
		return ret;
	}

	public Binding getBinding(Object pojo) throws InstantiationException,
			IllegalAccessException {
		return getBinding(pojo.getClass());
	}

	RequiredParametersMissingException makeRequiredParametersMissingException(
			HashSet<Field> missingFields, Class<?> clazz) {

		if (missingFields.size() == 0)
			return null;

		HashSet<String> missingParameters = new HashSet<String>();
		for (Field f : missingFields) {
			String queryStrfieldName = f.getName();
			QueryStringField anno = f.getAnnotation(QueryStringField.class);
			if (anno != null) {
				if (anno.name() != null && anno.name().length() > 0) {
					queryStrfieldName = anno.name();
				}
			}
			missingParameters.add(queryStrfieldName);
		}

		RequiredParametersMissingException ret = new RequiredParametersMissingException(
				missingParameters, clazz);
		return ret;
	}

	public void bind(String qstrin, Object pojo) throws Exception {
		Map<String, String> data = new HashMap<String, String>();
		for (String s : qstrin.split("&")) {
			if (s == null || s.length() == 0)
				continue;
			String[] kv = s.split("=", -1);
			if (kv.length != 2)
				continue;
			String fieldname = URLDecoder.decode(kv[0], "UTF-8");
			String value = URLDecoder.decode(kv[1], "UTF-8");
			data.put(fieldname, value);
		}
		this.bind(data, pojo, null);
	}

	public void bind(
			InMemoryMultipartEntryHandler inMemoryMultipartEntryHandler,
			Object pojo) throws UnsupportedEncodingException, Exception {
		this.bind(inMemoryMultipartEntryHandler.getMap(), pojo);

		this.bindMultipartFiles(inMemoryMultipartEntryHandler, pojo);
	}

	public void bindMultipartFiles(
			InMemoryMultipartEntryHandler inMemoryMultipartEntryHandler,
			Object pojo) throws InstantiationException, IllegalAccessException {
		Binding binding = this.getBinding(pojo);
		HashSet<Field> requiredFields = binding.getRequiredFieldsChecking();

		for (String key : inMemoryMultipartEntryHandler.getMultipartEntries()
				.keySet()) {

			ContentDisposition contentDisposition = inMemoryMultipartEntryHandler
					.getContentDisposition(key);
			if (contentDisposition == null) {
				continue;
			}
			String filename = contentDisposition
					.getDispositionParam("filename");
			byte[] bytes = inMemoryMultipartEntryHandler.getBytes(key);
			
			Field field = binding.getMultiparFileField(key);
			if (field != null) {
				MultipartFile mf = new MultipartFile(filename, bytes);
				field.set(pojo, mf);
			}
		}

	}

	public static Map<String, String> extractParameters(Request req) {
		Map<String, String> data = new HashMap<String, String>();
		for (String pn : req.getParameterNames()) {
			String value = req.getParameter(pn);
			data.put(pn, value);
		}
		return data;
	}

	public boolean ignoreRequiredCheck = false;

	public void bind(Request req, Object pojo) throws Exception {
		Map<String, String> data = extractParameters(req);
		this.bind(data, pojo, null);
	}

	public void bind(Request req, Object pojo, Set<String> allowedFields)
			throws Exception {
		Map<String, String> data = extractParameters(req);
		this.bind(data, pojo, allowedFields);
	}

	public void bind(Map<String, String> data, Object pojo) throws Exception {
		this.bind(data, pojo, null);
	}

	public void bind(Map<String, String> data, Object pojo,
			Set<String> allowedFields) throws Exception {
		if (data == null) {
			return;
		}
		Binding binding = this.getBinding(pojo);
		HashSet<Field> requiredFields = binding.getRequiredFieldsChecking();

		for (String pn : data.keySet()) {
			if (allowedFields != null && !allowedFields.contains(pn)) {
				continue;
			}
			String fieldname = pn;
			String value = data.get(pn);

			IQueryStringFieldParser parser = binding.getParser(fieldname);
			if (parser == null) {
				// System.out.println("parser == null: " + fieldname);
				continue;
			}

			if (value.length() == 0 && !parser.isCanBeBlank()) {
				continue;
			}
			Object parsedValue = null;
			try {
				parsedValue = parser.parse(value);

			} catch (Exception ex) {
				if (!parser.getIgnoreMalFormat()) {
					throw new QueryStringParseException(fieldname, value, ex);
				} else {
					continue;
				}
			}
			Field f = parser.getField();
			f.setAccessible(true);
			f.set(pojo, parsedValue);
			requiredFields.remove(parser.getField());
		}

		if (requiredFields.size() > 0 && !this.ignoreRequiredCheck) {
			throw this.makeRequiredParametersMissingException(requiredFields,pojo.getClass());
		}
		return;
	}

	public String toQueryString(Map<String, String> data)
			throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> entry : data.entrySet()) {
			sb.append(entry.getKey());
			sb.append("=");
			// sb.append(java.net.URLEncoder.encode(entry.getValue(), "utf-8"));
			sb.append(entry.getValue());
			sb.append("&");
		}
		String ret = sb.toString();
		// ret = java.net.URLEncoder.encode(ret, "utf-8");
		ret = UrlEscapers.urlFragmentEscaper().escape(ret);
		return ret;
	}

	// 只是开发的时候生成测试用例用，没做cache，如果需要生产用，需要加上cache功能。
	public String toQueryString(Object pojo, String dateFormatStr)
			throws IllegalArgumentException, IllegalAccessException,
			UnsupportedEncodingException {
		Map<String, String> data = this.toHashMap(pojo, dateFormatStr);
		return this.toQueryString(data);
		/*
		 * if (dateFormatStr == null) dateFormatStr = "yyyy-MM-dd HH:mm:ss";
		 * SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormatStr);
		 * 
		 * StringBuilder sb = new StringBuilder(); Class<? extends Object> clazz
		 * = pojo.getClass();
		 * 
		 * Field[] fields = FieldUtils.getAllFields(clazz); for (Field f :
		 * fields) { f.setAccessible(true); if
		 * (java.lang.reflect.Modifier.isStatic(f.getModifiers())) { continue; }
		 * QueryStringField anno = f.getAnnotation(QueryStringField.class); if
		 * (anno == null) { continue; } if (anno.hidden() == true) { continue; }
		 * String queryStrfieldName = f.getName(); if (anno != null) { if
		 * (anno.name() != null && anno.name().length() > 0) { queryStrfieldName
		 * = anno.name(); } } String k =
		 * java.net.URLEncoder.encode(queryStrfieldName, "UTF-8"); String v =
		 * ""; if (f.getType() == long.class) { v =
		 * Long.toString(f.getLong(pojo)); } else if (f.getType() == int.class)
		 * { v = Long.toString(f.getLong(pojo)); } else if (f.getType() ==
		 * boolean.class) { v = Boolean.toString(f.getBoolean(pojo)); } else if
		 * (f.getType() == float.class) { v = Float.toString(f.getFloat(pojo));
		 * } else if (f.getType() == double.class) { v =
		 * Double.toString(f.getDouble(pojo)); } else if
		 * (Date.class.isAssignableFrom(f.getType())) { Date d = (Date)
		 * f.get(pojo); if (d == null) { v = ""; } else { v =
		 * dateFormatter.format(d); } } else if
		 * (f.getType().isAssignableFrom(Date.class)) { Date d = (Date)
		 * f.get(pojo); if (d == null) { v = ""; } else { v =
		 * dateFormatter.format(d); } } else { Object o = f.get(pojo); if (o ==
		 * null) v = ""; else v =
		 * java.net.URLEncoder.encode(f.get(pojo).toString(), "UTF-8"); }
		 * sb.append(k); sb.append("="); sb.append(v); sb.append("&"); }
		 * 
		 * String ret = sb.toString(); ret.replaceAll("\\+", "%20"); return ret;
		 */
	}

	public HashMap<String, String> toHashMap(Object pojo, String dateFormatStr)
			throws IllegalArgumentException, IllegalAccessException,
			UnsupportedEncodingException {
		if (dateFormatStr == null)
			dateFormatStr = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormatStr);
		HashMap<String, String> ret = new HashMap<String, String>();
		// StringBuilder sb = new StringBuilder();
		Class<? extends Object> clazz = pojo.getClass();

		Field[] fields = FieldUtils.getAllFields(clazz);
		for (Field f : fields) {
			f.setAccessible(true);
			if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
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
			String k = queryStrfieldName;
			String v = "";
			if (f.getType() == long.class) {
				v = Long.toString(f.getLong(pojo));
			} else if (f.getType() == int.class) {
				v = Long.toString(f.getLong(pojo));
			} else if (f.getType() == boolean.class) {
				v = Boolean.toString(f.getBoolean(pojo));
			} else if (f.getType() == float.class) {
				v = Float.toString(f.getFloat(pojo));
			} else if (f.getType() == double.class) {
				v = Double.toString(f.getDouble(pojo));
			} else if (Date.class.isAssignableFrom(f.getType())) {
				Date d = (Date) f.get(pojo);
				if (d == null) {
					v = "";
				} else {
					v = dateFormatter.format(d);
				}
			} else if (f.getType().isAssignableFrom(Date.class)) {
				Date d = (Date) f.get(pojo);
				if (d == null) {
					v = "";
				} else {
					v = dateFormatter.format(d);
				}
			} else if (f.getType() == MultipartFile.class) {
				MultipartFile mf = (MultipartFile) f.get(pojo);
				v = "filename=" + mf.getFilename().replace("\"", "") + ",length=" + mf.getBytes().length;
			} else if (f.getType().isEnum()) {
				
				Enum<?> value = (Enum<?>) f.get(pojo);
				v = value.name();
			} else {
				Object o = f.get(pojo);
				if (o == null)
					v = "";
				else {
					// v = java.net.URLEncoder.encode(f.get(pojo).toString(),
					// "UTF-8");
					v = f.get(pojo).toString();
				}
			}
			ret.put(k, v);

		}

		return ret;
	}

	/**
	 * 改用QueryBuilder.JDBCWhere
	 **/
	@Deprecated
	public static class JDBCWhere {
		String whereClause = "";

		public String getWhereClause() {
			return whereClause;
		}

		public void setWhereClause(String whereClause) {
			this.whereClause = whereClause;
		}

		public Object[] getParams() {
			return params;
		}

		public void setParams(Object[] params) {
			this.params = params;
		}

		Object[] params = new Object[0];

		public JDBCWhere() {
		}

		public JDBCWhere(String where, Object[] params) {
			this.whereClause = where;
			this.params = params;
		}

		public void setParamters(PreparedStatement stmt) throws SQLException {
			for (int i = 0; i < this.params.length; ++i) {
				stmt.setObject(i + 1, this.params[i]);
			}

		}
	}

	/**
	 * 改用QueryBuilder.JDBCOrderBy
	 **/
	@Deprecated
	public static class JDBCOrderBy {
		private String orderByClause = "";

		public String getOrderByClause() {
			return orderByClause;
		}

		public void setOrderByClause(String orderByClause) {
			this.orderByClause = orderByClause;
		}
	}

	/**
	 * 改用QueryBuilder.genJDBCOrderBy
	 **/
	@Deprecated
	public JDBCOrderBy genJDBCOrderBy(Map<String, String> data,
			Class<? extends Object> clazz, Set<String> allowedFields)
			throws Exception {
		// TODO add check for invalid columns, invalid direction(only desc and
		// asc are allowed), and
		// injection attack.
		JDBCOrderBy ret = new JDBCOrderBy();
		if (data.containsKey("order_by")) {
			ret.orderByClause = "order by " + data.get("order_by");
		}
		return ret;
	}

	/**
	 * 改用QueryBuilder.genJDBCWhere
	 **/
	@Deprecated
	public JDBCWhere genJDBCWhere(Map<String, String> data,
			Class<? extends Object> clazz, Set<String> allowedFields)
			throws Exception {
		if (data == null) {
			return new JDBCWhere();
		}
		StringBuilder sb = new StringBuilder();
		LinkedList<Object> params = new LinkedList<Object>();

		Binding binding = this.getBinding(clazz);
		String conj = "WHERE ";
		for (String key : data.keySet()) {
			if (allowedFields != null && !allowedFields.contains(key)) {
				continue;
			}
			String fieldname = "";
			String op = "";
			boolean needValue = true;
			if (key.startsWith("eq__")) {
				fieldname = key.substring(4);
				op = " = ? ";
			} else if (key.startsWith("lt__")) {
				fieldname = key.substring(4);
				op = " < ? ";
			} else if (key.startsWith("lte__")) {
				fieldname = key.substring(5);
				op = " <= ? ";
			} else if (key.startsWith("gt__")) {
				fieldname = key.substring(4);
				op = " > ? ";
			} else if (key.startsWith("gte__")) {
				fieldname = key.substring(5);
				op = " >= ? ";
			} else if (key.startsWith("neq__")) {
				fieldname = key.substring(5);
				op = " != ? ";
			} else if (key.startsWith("isnull__")) {
				fieldname = key.substring(8);
				op = " IS NULL ";
				needValue = false;
			} else if (key.startsWith("isblank__")) {
				fieldname = key.substring(8);
				op = " = '' ";
				needValue = false;
			} else {
				continue;
			}
			Object parsedValue = null;
			if (needValue) {
				String value = data.get(key);

				IQueryStringFieldParser parser = binding.getParser(fieldname);
				if (parser == null) {
					continue;
				}

				if (value.length() == 0) {
					continue;
				}
				try {
					parsedValue = parser.parse(value);

				} catch (Exception ex) {
					if (!parser.getIgnoreMalFormat()) {
						throw ex;
					} else {
						continue;
					}
				}
			}

			sb.append(conj);
			sb.append(fieldname);
			sb.append(op);

			conj = " AND \n";
			if (needValue) {
				params.add(parsedValue);
			}

		}
		return new JDBCWhere(sb.toString(), params.toArray());
	}

	/**
	 * 改用QueryBuilder.genJDBCWhere
	 **/
	@Deprecated
	public JDBCWhere genJDBCWhere(Request req, Class<? extends Object> clazz,
			Set<String> allowedFields) throws Exception {

		Map<String, String> data = extractParameters(req);
		return this.genJDBCWhere(data, clazz, allowedFields);
	}

	/**
	 * 改用QueryBuilder.genJDBCWhere
	 **/
	@Deprecated
	public JDBCWhere genJDBCWhere(Request req, Class<? extends Object> clazz)
			throws Exception {
		return this.genJDBCWhere(req, clazz, null);
	}
}
