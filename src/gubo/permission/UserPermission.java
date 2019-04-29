package gubo.permission;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name="v_user_permission")
public class UserPermission {

	@Column()
	public long user_id;
	@Column()
	public long role_id;
	@Column()
	public long permission_id;
	@Column()
	public String permission;
	@Column()
	public String permission_code;
	
}
