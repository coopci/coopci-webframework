package gubo.db;

import gubo.http.querystring.IQueryStringFieldParser;
import gubo.http.querystring.QueryStringField;
import gubo.http.querystring.parsers.BigDecimalParser;
import gubo.http.querystring.parsers.BooleanFieldParser;
import gubo.http.querystring.parsers.DatetimeParser;
import gubo.http.querystring.parsers.DoubleFieldParser;
import gubo.http.querystring.parsers.FloatFieldParser;
import gubo.http.querystring.parsers.IntegerFieldParser;
import gubo.http.querystring.parsers.LongFieldParser;
import gubo.http.querystring.parsers.NullParser;
import gubo.http.querystring.parsers.StringFieldParser;
import gubo.http.querystring.parsers.TimeParser;
import gubo.http.querystring.parsers.TimestampParser;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Column;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.glassfish.grizzly.http.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 根据给定的HashMap 建立 sql 查询字符串。
 * jdbc相关的是遗留功能，以后会被取代。 
 **/
public class QueryBuilder {
	public static Logger logger = LoggerFactory
			.getLogger(QueryBuilder.class);

	public static class Binding {
		Class<?> clazz;

		// key 是数据库表的列的名字，不是pojo的Field的名字。
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

		public void tryAddField(Field f) throws InstantiationException,
				IllegalAccessException {
			if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
				return;
			}

			Column anno = f.getAnnotation(Column.class);

			if (anno == null) {
				// System.out.println("anno == null: " + f.getName());

				logger.debug("anno == null, class: {}, field name: {} ",
						clazz.getName(), f.getName());
				return;
			}

			String colName = f.getName();

			if (anno != null) {
				if (anno.name() != null && anno.name().length() > 0) {
					colName = anno.name();
				}
			}

			// System.out.println("queryStrfieldName = " + queryStrfieldName);

			Class<? extends IQueryStringFieldParser> deserializerClass = NullParser.class;
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
				} else if (f.getType() == Time.class) {
					deserializerClass = TimeParser.class;
				} else if (f.getType() == BigDecimal.class) {
					deserializerClass = BigDecimalParser.class;
				} else if (f.getType() == Timestamp.class) {
                    deserializerClass = TimestampParser.class;
                }
			}

			// System.out.println("deserializerClass = " + deserializerClass);

			IQueryStringFieldParser deserializerObj = deserializerClass
					.newInstance();
			if (anno != null) {
				deserializerObj.setIgnoreMalFormat(false);
				deserializerObj.setCanBeBlank(false);
				deserializerObj.setDoTrim(true);
			}

			deserializerObj.setField(f);
			this._cachedParses.put(colName, deserializerObj);

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

	private Binding getBinding(Class<? extends Object> clazz)
			throws InstantiationException, IllegalAccessException {
		Binding ret = _cachedBindings.get(clazz);
		if (ret != null) {
			return ret;
		}
		ret = constructBinding(clazz);
		_cachedBindings.put(clazz, ret);
		return ret;
	}


	private static Map<String, String> extractParameters(Request req) {
		Map<String, String> data = new HashMap<String, String>();
		for (String pn : req.getParameterNames()) {
			String value = req.getParameter(pn);
			data.put(pn, value);
		}
		return data;
	}

	public boolean ignoreRequiredCheck = false;

	
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
				stmt.setObject(i+1, this.params[i]);
			}
			
		}
	}

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
     *  取出 key "order_by" 的值。
     **/
    public JDBCOrderBy genJDBCOrderBy(Map<String, String> data,
            Class<? extends Object> clazz, Set<String> allowedFields)
            throws Exception {
        // TODO add check for invalid columns, invalid direction(only desc and asc are allowed), and
        // injection attack.
        JDBCOrderBy ret = new JDBCOrderBy();
        if (data.containsKey("order_by")) {
            ret.orderByClause = "order by " + data.get("order_by");
        }
        return ret;
    }
	/**
	 * eq__a=& 这种写法的效果是忽略 eq__a 如果筛选a==''，需要写: isblank__a=&
	 * 
	 * @param clazz
	 *            中作为筛选的字段需要用 {@link QueryStringField} 标注才行。
	 * 
	 *            目前支持的操作符包括: eq__, lt__, lte__, gt__, gte__, neq__
	 **/
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
				fieldname = key.substring("isblank__".length());
				op = " = '' ";
				needValue = false;
			} else if (key.startsWith("startswith__")) {
                fieldname = key.substring("startswith__".length());
                op = " like CONCAT(?, '%') ";
            } else if (key.startsWith("contains__")) {
                fieldname = key.substring("contains__".length());
                op = " like CONCAT('%', ?, '%') ";
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
	
	public JDBCWhere genJDBCWhere2(Map<String, Object> data,
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
				fieldname = key.substring("isblank__".length());
				op = " = '' ";
				needValue = false;
			} else if (key.startsWith("startswith__")) {
                fieldname = key.substring("startswith__".length());
                op = " like CONCAT(?, '%') ";
            } else if (key.startsWith("contains__")) {
                fieldname = key.substring("contains__".length());
                op = " like CONCAT('%', ?, '%') ";
            } else {
				continue;
			}
			Object parsedValue = null;
			if (needValue) {
				Object oValue = data.get(key);
				if (oValue.getClass() == String.class) {
					String value = (String)oValue;
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
				} else {
					parsedValue = oValue;
				}
			}

			sb.append(conj);
			sb.append(fieldname);
			sb.append(op);

			conj = "AND \n";
			if (needValue) {
				params.add(parsedValue);
			}

		}
		return new JDBCWhere(sb.toString(), params.toArray());
	}

	public JDBCWhere genJDBCWhere(Request req, Class<? extends Object> clazz,
			Set<String> allowedFields) throws Exception {

		Map<String, String> data = extractParameters(req);
		return this.genJDBCWhere(data, clazz, allowedFields);
	}

	public JDBCWhere genJDBCWhere(Request req, Class<? extends Object> clazz)
			throws Exception {
		return this.genJDBCWhere(req, clazz, null);
	}
}
