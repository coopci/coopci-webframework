package springless.http.grizzly.handlers;

import springless.db.ISimplePoJo;
import springless.http.grizzly.NannyHttpHandler;
import springless.jdbc.mapping.ResultSetMapper;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Entity;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

/**
 *	列出被@Entity的类对应的表中的所有数据。
 * 
 **/
public class ListAllHandler extends NannyHttpHandler {
	
	Class<? extends ISimplePoJo> clazz;
	public ListAllHandler(Class<? extends ISimplePoJo> clazz) {
		this.clazz = clazz;
	}
	
	
	@Override
	public Object doGet(Request request, Response response) throws Exception {
		this.authCheck(request);
		Entity entity = clazz.getAnnotation(Entity.class);
		String tablename = entity.name();
		if (tablename == null || tablename.length() == 0) {
			tablename = clazz.getName();
		}
		Connection dbconn = this.getConnection();
		try {
			dbconn.setAutoCommit(true);

			List<?> data = ResultSetMapper.loadPoJoList(dbconn, clazz,
					"select * from `" + tablename + "`;");
			HashMap<String, Object> ret = this.getOKResponse();
			ret.put("data", data);
			return ret;

		} finally {
			dbconn.close();
		}
	}

}
