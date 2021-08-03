package springless.http.grizzly.handlers;

import springless.db.ISimplePoJo;
import springless.http.grizzly.NannyHttpHandler;
import springless.http.querystring.QueryStringBinder;
import springless.http.querystring.QueryStringField;
import springless.jdbc.mapping.ResultSetMapper;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

/**
 * 列出被@Entity的类对应的表中的数据。 带有筛选功能，筛选功能用 {@link QueryStringBinder } 的 genJDBCWhere
 * 实现。 作为筛选的字段需要用 {@link QueryStringField} 标注才行。
 * 
 * 筛选条件：
 * 目前支持的操作符包括: eq__, lt__, lte__, gt__, gte__, neq__
 * eq__a=& 这种写法的效果是忽略 eq__a 如果筛选a==''，需要写: isblank__a=&
 * 
 * 排序:
 * 可以用 order_by  来指定按哪些字段排序，例如 col1， col2 desc ...
 * 
 **/
public class FilteredListHandler extends NannyHttpHandler {

	Class<? extends ISimplePoJo> clazz;

	public FilteredListHandler(Class<? extends ISimplePoJo> clazz) {
		this.clazz = clazz;
	}

	protected final Object doFilter(Map<String, String> params)
			throws Exception {

		Connection dbconn = this.getConnection();
		try {
			dbconn.setAutoCommit(true);

			List<?> data = ResultSetMapper.loadPoJoList(dbconn, clazz, params);
			HashMap<String, Object> ret = this.getOKResponse();
			ret.put("data", data);
			return ret;

		} finally {
			dbconn.close();
		}
	}

	/**
	 * Subclasses can override this method to do custom check, add extra
	 * conditions, then call super.doGet
	 * 
	 **/
	@Override
	public Object doGet(Request request, Response response) throws Exception {
		this.authCheck(request);
		Map<String, String> conditions = QueryStringBinder
				.extractParameters(request);
		Object ret = this.doFilter(conditions);
		return ret;
	}

}
