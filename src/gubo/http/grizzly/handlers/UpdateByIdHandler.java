package gubo.http.grizzly.handlers;

import java.sql.Connection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.persistence.Entity;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import db.ShadowMerchant;
import api.handlers.BaseApiHander;
import gubo.db.ISimplePoJo;
import gubo.http.grizzly.ApiHttpHandler;
import gubo.http.querystring.QueryStringBinder;
import gubo.jdbc.mapping.ResultSetMapper;
import gubo.jdbc.mapping.UpdateStatementGenerator;
/**
 *	对指定 被@Entity的类对应的表 做update。
 *  只对作为参数给出的id的行做更新。
 * 
 **/
public class UpdateByIdHandler extends ApiHttpHandler {
	private final Class<? extends ISimplePoJo> clazz;
	private final HashSet<String> allowedFields;
	public HashSet<String> getAllowedFields() {
		return allowedFields;
	}


	public HashMap<String, String> getOverrideFields() {
		return overrideFields;
	}


	private final HashMap<String, String> overrideFields;
	/**
	 *
	 * @param allowedFields null表示可以对任何列做更新，长度为0的数组表示不允许对任何列做更新，其他情况表示可以对指定的列做更新。
	 * 
	 **/
	public UpdateByIdHandler(Class<? extends ISimplePoJo> clazz, String[] allowedFields, HashMap<String, String> overrideFields) {
		this.clazz = clazz;
		if (allowedFields == null) {
			this.allowedFields = null;	
		} else {
			this.allowedFields = new HashSet<String>();
			for (String f : allowedFields) {
				this.allowedFields.add(f);
			}
		}
		this.overrideFields = overrideFields;
	}
	
	
	@Override
	public Object doPost(Request request, Response response) throws Exception {
		String id = this.getRequiredStringParameter(request, "id");
		Entity entity = clazz.getAnnotation(Entity.class);
		String tablename = entity.name();
		if (tablename == null || tablename.length() == 0) {
			tablename = clazz.getName();
		}
		Connection dbconn = this.getConnection();
		try {
			dbconn.setAutoCommit(true);
			ResultSetMapper<ShadowMerchant> mapper = new ResultSetMapper<ShadowMerchant>();

			Object pojo = mapper.loadPojo(dbconn, clazz,
					"select * from `" + tablename + "` where id=?;", id);
			
			
			QueryStringBinder binder = new QueryStringBinder();
			binder.ignoreRequiredCheck=true;
			binder.bind(request, pojo, this.getAllowedFields());
			
			binder.bind(this.getOverrideFields(), pojo);
			
			
			UpdateStatementGenerator.update(dbconn, pojo);
			
			HashMap<String, Object> ret = this.getOKResponse();
			ret.put("data", pojo);
			return ret;

		} finally {
			dbconn.close();
		}
	}

}
