package gubo.http.grizzly.handlers;

import gubo.db.IConnectionProvider;
import gubo.db.ISimplePoJo;
import gubo.http.grizzly.ApiHttpHandler;
import gubo.http.querystring.QueryStringBinder;
import gubo.http.querystring.QueryStringBinder.JDBCWhere;
import gubo.http.querystring.QueryStringField;
import gubo.jdbc.mapping.ResultSetMapper;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import db.ShadowMerchant;

/**
 * 列出被@Entity的类对应的表中的数据。 带有筛选功能，筛选功能用 {@link QueryStringBinder } 的 genJDBCWhere
 * 实现。 作为筛选的字段需要用 {@link QueryStringField} 标注才行。
 **/
public class FilteredListHandler extends ApiHttpHandler {

	Class<? extends ISimplePoJo> clazz;

	public FilteredListHandler(Class<? extends ISimplePoJo> clazz,
			IConnectionProvider connectionProvider) {
		this.clazz = clazz;
		this.setConnectionProvider(connectionProvider);
	}

	final Object doFilter(Map<String, String> params) throws Exception {

		Entity entity = clazz.getAnnotation(Entity.class);
		String tablename = entity.name();
		if (tablename == null || tablename.length() == 0) {
			tablename = clazz.getName();
		}

		QueryStringBinder binder = new QueryStringBinder();
		JDBCWhere jdbcWhere = binder.genJDBCWhere(params, clazz, null);

		Connection dbconn = this.getConnection();
		try {
			dbconn.setAutoCommit(true);
			ResultSetMapper<ShadowMerchant> mapper = new ResultSetMapper<ShadowMerchant>();

			List<?> data = mapper.loadPojoList(dbconn, clazz, "select * from `"
					+ tablename + "` " + jdbcWhere.getWhereClause(),
					jdbcWhere.getParams());
			HashMap<String, Object> ret = this.getOKResponse();
			ret.put("data", data);
			return ret;

		} finally {
			dbconn.close();
		}
	}

	/**
	 * Subclasses can override this method to do custom check, add extra
	 * conditions, then call super.doUpdate
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
