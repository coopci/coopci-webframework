package gubo.http.querystring;

import gubo.exceptions.RequiredParametersMissingException;
import gubo.http.querystring.parsers.BooleanFieldParser;
import gubo.http.querystring.parsers.DatetimeParser;
import gubo.http.querystring.parsers.DoubleFieldParser;
import gubo.http.querystring.parsers.FloatFieldParser;
import gubo.http.querystring.parsers.IntegerFieldParser;
import gubo.http.querystring.parsers.LongFieldParser;
import gubo.http.querystring.parsers.NullParser;
import gubo.http.querystring.parsers.StringFieldParser;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import org.glassfish.grizzly.http.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class QueryStringBinder {
	public static Logger logger = LoggerFactory.getLogger(QueryStringBinder.class);

	public static class Binding {
		Class<?> clazz;

		// key 是 querystring中的字段名，不是Field的名字。
		ConcurrentHashMap<String, IQueryStringFieldParser> _cachedParses = new ConcurrentHashMap<String, IQueryStringFieldParser>();

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
	}

	public Binding constructBinding(Object pojo) throws InstantiationException, IllegalAccessException {
		Binding binding = new Binding();
		Class<? extends Object> clazz = pojo.getClass();
		binding.clazz = clazz;

		Field[] fields = clazz.getFields();
		for (Field f : fields) {
			if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
				continue;
			}

			QueryStringField anno = f.getAnnotation(QueryStringField.class);

			if (anno == null) {
				// System.out.println("anno == null: " + f.getName());

				logger.debug("anno == null, class: {}, field name: {} ", clazz.getName(), f.getName());
				continue;
			}

			String queryStrfieldName = f.getName();

			if (anno != null) {
				if (anno.name() != null && anno.name().length() > 0) {
					queryStrfieldName = anno.name();
				}
				if (anno.required()) {
					binding.requiredFields.add(f);
				}

			}

			// System.out.println("queryStrfieldName = " + queryStrfieldName);

			Class<? extends IQueryStringFieldParser> deserializerClass = anno.deserializer();
			if (deserializerClass == NullParser.class) {
				if (f.getType() == String.class) {
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
				}
			}

			// System.out.println("deserializerClass = " + deserializerClass);

			IQueryStringFieldParser deserializerObj = deserializerClass.newInstance();
			if (anno != null) {
				deserializerObj.setIgnoreMalFormat(anno.ignoreMalFormat());
			}

			deserializerObj.setField(f);
			binding._cachedParses.put(queryStrfieldName, deserializerObj);
		}
		return binding;
	}

	static ConcurrentHashMap<Class<?>, Binding> _cachedBindings = new ConcurrentHashMap<Class<?>, Binding>();

	public Binding getBinding(Object pojo) throws InstantiationException, IllegalAccessException {
		Binding ret = _cachedBindings.get(pojo.getClass());
		if (ret != null) {
			return ret;
		}
		ret = constructBinding(pojo);
		_cachedBindings.put(pojo.getClass(), ret);

		return ret;

	}

	RequiredParametersMissingException makeRequiredParametersMissingException(HashSet<Field> missingFields) {

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

		RequiredParametersMissingException ret = new RequiredParametersMissingException(missingParameters);
		return ret;
	}

	public void bind(String qstrin, Object pojo) throws Exception {
		Binding binding = this.getBinding(pojo);
		HashSet<Field> requiredFields = binding.getRequiredFieldsChecking();

		for (String s : qstrin.split("&")) {

			if (s == null || s.length() == 0)
				continue;

			String[] kv = s.split("=", -1);
			if (kv.length != 2)
				continue;

			String fieldname = URLDecoder.decode(kv[0], "UTF-8");
			String value = URLDecoder.decode(kv[1], "UTF-8");

			IQueryStringFieldParser parser = binding.getParser(fieldname);
			if (parser == null) {
				// System.out.println("parser == null: " + fieldname);
				continue;
			}
			Object parsedValue = null;
			try {
				parsedValue = parser.parse(value);

			} catch (Exception ex) {
				if (!parser.getIgnoreMalFormat()) {
					throw ex;
				} else {
					continue;
				}
			}
			parser.getField().set(pojo, parsedValue);
			requiredFields.remove(parser.getField());
		}

		if (requiredFields.size() > 0) {
			throw this.makeRequiredParametersMissingException(requiredFields);

		}
		return;
	}

	
	
	public void bind(Request req, Object pojo) throws Exception {
		Binding binding = this.getBinding(pojo);
		HashSet<Field> requiredFields = binding.getRequiredFieldsChecking();

		
		for (String pn : req.getParameterNames()) {

			String fieldname = pn;
			String value = req.getParameter(pn);

			IQueryStringFieldParser parser = binding.getParser(fieldname);
			if (parser == null) {
				// System.out.println("parser == null: " + fieldname);
				continue;
			}
			Object parsedValue = null;
			try {
				parsedValue = parser.parse(value);

			} catch (Exception ex) {
				if (!parser.getIgnoreMalFormat()) {
					throw ex;
				} else {
					continue;
				}
			}
			parser.getField().set(pojo, parsedValue);
			requiredFields.remove(parser.getField());
		}

		if (requiredFields.size() > 0) {
			throw this.makeRequiredParametersMissingException(requiredFields);

		}
		return;
	}

	
	
	// 只是开发的时候生成测试用例用，没做cache，如果需要生产用，需要加上cache功能。
	public String toQueryString(Object pojo, String dateFormatStr) throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException {
		if (dateFormatStr == null)
			dateFormatStr = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormatStr);
		
		StringBuilder sb = new StringBuilder();
		Binding binding = new Binding();
		Class<? extends Object> clazz = pojo.getClass();
		binding.clazz = clazz;

		Field[] fields = clazz.getFields();
		for (Field f : fields) {
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
			String k = java.net.URLEncoder.encode(queryStrfieldName, "UTF-8");
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
				Date d = (Date)f.get(pojo);
				if (d == null) {
					v = "";
				} else {
					v = dateFormatter.format(d);
				}
			} else if (f.getType().isAssignableFrom(Date.class)) {
				Date d = (Date)f.get(pojo);
				if (d == null) {
					v = "";
				} else {
					v = dateFormatter.format(d);
				}
			} else {
				Object o = f.get(pojo);
				if (o == null)
					v = "";
				else
					v = java.net.URLEncoder.encode(f.get(pojo).toString(), "UTF-8");
			}
			sb.append(k);
			sb.append("=");
			sb.append(v);
			sb.append("&");
		}

		String ret = sb.toString();
		ret.replaceAll("\\+", "%20");
		return ret;
	}

}
