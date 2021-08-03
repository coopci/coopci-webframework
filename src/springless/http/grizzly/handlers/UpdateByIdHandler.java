package springless.http.grizzly.handlers;

import springless.db.ISimplePoJo;
import springless.exceptions.ObjectNotFoundException;
import springless.http.grizzly.NannyHttpHandler;
import springless.http.querystring.QueryStringBinder;
import springless.jdbc.mapping.ResultSetMapper;
import springless.jdbc.mapping.UpdateStatementGenerator;

import java.sql.Connection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.persistence.Entity;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

/**
 *	对指定 被@Entity的类对应的表 做update。
 *  只对作为参数给出的id的行做更新。
 * 
 **/
public class UpdateByIdHandler extends NannyHttpHandler {
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
	public UpdateByIdHandler(Class<? extends ISimplePoJo> clazz, 
			String[] allowedFields, HashMap<String, String> overrideFields) {
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
	
	
	final protected Object doUpdate(String id, Map<String, String> data) throws Exception {
		Entity entity = clazz.getAnnotation(Entity.class);
		String tablename = entity.name();
		if (tablename == null || tablename.length() == 0) {
			tablename = clazz.getName();
		}
		Connection dbconn = this.getConnection();
		try {
			dbconn.setAutoCommit(true);
			
			Object pojo = ResultSetMapper.loadPoJo(dbconn, clazz,
					"select * from `" + tablename + "` where id=?;", id);
			
			if (pojo == null) {
				throw new ObjectNotFoundException("Not Found: " + tablename + ", id=" + id);
			}
			QueryStringBinder binder = new QueryStringBinder();
			binder.ignoreRequiredCheck=true;
			binder.bind(data, pojo, this.getAllowedFields());
			
			binder.bind(this.getOverrideFields(), pojo, null);
			
			
			UpdateStatementGenerator.update(dbconn, pojo);
			
			HashMap<String, Object> ret = this.getOKResponse();
			ret.put("data", pojo);
			return ret;

		} finally {
			dbconn.close();
		}
	}
	
	/**
	 *	Subclasses can override this method to do custom check, then call super.doUpdate 
	 *  
	 **/
	@Override
	public Object doPost(Request request, Response response) throws Exception {
		this.authCheck(request);
		Map<String, String> data = QueryStringBinder.extractParameters(request);
		String id = this.getRequiredStringParameter(request, "id");
		
		Object ret = this.doUpdate(id, data);
		return ret;
	}

}
