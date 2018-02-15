package gubo.http.grizzly.handlers;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Entity;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import db.ShadowMerchant;
import api.handlers.BaseApiHander;
import gubo.db.ISimplePoJo;
import gubo.jdbc.mapping.ResultSetMapper;
/**
 *	列出被@Entity的类对应的表中的所有数据。
 * 
 **/
public class ListAllHandler extends BaseApiHander {
	
	Class<? extends ISimplePoJo> clazz;
	public ListAllHandler(Class<? extends ISimplePoJo> clazz) {
		this.clazz = clazz;
	}
	
	
	@Override
	public Object doGet(Request request, Response response) throws Exception {
		
		Entity entity = clazz.getAnnotation(Entity.class);
		String tablename = entity.name();
		if (tablename == null || tablename.length() == 0) {
			tablename = clazz.getName();
		}
		Connection dbconn = this.getConnection();
		try {
			dbconn.setAutoCommit(true);
			ResultSetMapper<ShadowMerchant> mapper = new ResultSetMapper<ShadowMerchant>();

			List<?> data = mapper.loadPojoList(dbconn, clazz,
					"select * from `" + tablename + "`;");
			HashMap<String, Object> ret = this.getOKResponse();
			ret.put("data", data);
			return ret;

		} finally {
			dbconn.close();
		}
	}

}
