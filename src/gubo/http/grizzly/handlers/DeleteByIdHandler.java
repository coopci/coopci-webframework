package gubo.http.grizzly.handlers;

import gubo.db.IConnectionProvider;
import gubo.db.ISimplePoJo;
import gubo.http.grizzly.ApiHttpHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;

import javax.persistence.Entity;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

/**
 * 对指定 被@Entity的类对应的表 做delete。 只对作为参数给出的id的行做更新。
 * 
 **/
public class DeleteByIdHandler extends ApiHttpHandler {
	private final Class<? extends ISimplePoJo> clazz;

	public DeleteByIdHandler(Class<? extends ISimplePoJo> clazz,
			IConnectionProvider connectionProvider) {
		this.clazz = clazz;
		this.setConnectionProvider(connectionProvider);
	}

	@Override
	public Object doPost(Request request, Response response) throws Exception {
		this.authCheck(request);
		String id = this.getRequiredStringParameter(request, "id");
		Entity entity = clazz.getAnnotation(Entity.class);
		String tablename = entity.name();
		if (tablename == null || tablename.length() == 0) {
			tablename = clazz.getName();
		}
		Connection dbconn = this.getConnection();
		try {
			dbconn.setAutoCommit(true);
			PreparedStatement stmt = dbconn.prepareStatement("delete from "
					+ tablename + " where id =?");
			stmt.setObject(1, id);
			stmt.execute();
			HashMap<String, Object> ret = this.getOKResponse();
			return ret;
		} finally {
			dbconn.close();
		}
	}

}
