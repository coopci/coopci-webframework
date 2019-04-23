package gubo.permission;

import gubo.db.DaoManager;
import gubo.db.SimplePoJoDAO;
import gubo.exceptions.PermissionDeniedException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


// T 是 用户类的class
public class PermissionManager {

	// 检查userid是否具有permission。
	public void checkPermission(Connection dbconn, long userid, String permission) throws SQLException, PermissionDeniedException {
		DaoManager daoManager = new DaoManager();
		
		SimplePoJoDAO dao = daoManager.getDao(UserPermission.class);
		List<UserPermission> upList = dao.loadPojoList(dbconn, "select * from v_user_permission where user_id=? and permission = ? ", userid, permission);
		if(upList.isEmpty()) {
			throw new PermissionDeniedException("Permission: " + permission + ", user_id: " + userid);
		}
		
	}
}
