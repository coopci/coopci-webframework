package gubo.permission;

import javax.persistence.Entity;

@Entity(name="v_user_permission")
public class UserPermission {

	public long user_id;
	public long role_id;
	public long permission_id;
	public String permission;
	
}
