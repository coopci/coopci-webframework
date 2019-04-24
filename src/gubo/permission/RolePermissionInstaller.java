package gubo.permission;

import gubo.db.DaoManager;
import gubo.db.SimplePoJoDAO;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 把指定的role/permission 组合 insert到数据库。
 * 不从数据库中删除已经存在但是本次没有指定的 role/permission 组合。
 **/ 
public class RolePermissionInstaller {
	private static Logger logger = LoggerFactory.getLogger(RolePermissionInstaller.class);
	public static class RolePermissionSpec {
		List<Permission> permissions;
		
		/**
		 * [0]  是role.name, [1]是permission.code 
		 **/
		List<String[]> rolePermissions;
		
		public void set(Permission ... permissions) {
			this.permissions = new LinkedList<Permission>();
			for (Permission p : permissions) {
				this.permissions.add(p);
			}
		}
		
		/**
		 * 
		 * @param rolePermissions: 角色，权限，角色，权限 ... 
		 * 
		 * */
		public void set(String ... rolePermissions) {
			this.rolePermissions = new LinkedList<String[]>();
			for (int i = 0; i < rolePermissions.length; i+=2) {
				String role = rolePermissions[i];
				String permCode = rolePermissions[i+1];
				String[] entry = new String[2];
				entry[0] = role;
				entry[1] = permCode;
				this.rolePermissions.add(entry);
			}
		}
	}
	
	public void install(Connection dbconn, RolePermissionSpec spec) throws Exception {
		
		DaoManager daoManager = new DaoManager();
		
		SimplePoJoDAO permissionDao = daoManager.getDao(Permission.class);
		SimplePoJoDAO roleDao = daoManager.getDao(Role.class);
		SimplePoJoDAO rpDao = daoManager.getDao(RolePermission.class);
		
		for (Permission p : spec.permissions) {
			Permission loadedPerm = permissionDao.loadPoJoByUniqueKey(dbconn, "code", p.code);
			if (loadedPerm==null) {
				permissionDao.insert(dbconn, p);
			}
			
		}
		
		for (String[] rp : spec.rolePermissions) {
			
			// role.name
			String role = rp[0];
			String permCode = rp[1];
			Role loadedRole = roleDao.loadPoJoByUniqueKey(dbconn, "name", role);
			if (loadedRole==null) {
				loadedRole = new Role(role);
				roleDao.insert(dbconn, loadedRole);
			}
			Permission loadedPerm = permissionDao.loadPoJoByUniqueKey(dbconn, "code", permCode);
			
			RolePermission rolePermission = new RolePermission();
			rolePermission.permission_id = loadedPerm.id;
			rolePermission.role_id = loadedRole.id;
			
			try {
				rpDao.insert(dbconn, rolePermission);
			} catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException ex) {
				if (ex.getMessage().startsWith("Duplicate entry ")) {
					// 如果已经存在就不用管了。	
				} else {
					logger.error("insert rolePermission 失败:", ex);	
				}
			} catch (Exception ex) {
				logger.error("insert rolePermission 失败:", ex);
			}
		}
		
	}
	
}
