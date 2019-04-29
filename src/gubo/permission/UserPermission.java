package gubo.permission;

import javax.persistence.Column;
import javax.persistence.Entity;

import gubo.http.querystring.QueryStringField;

@Entity(name="v_user_permission")
public class UserPermission {

	@Column()
	@QueryStringField()
	public long user_id;
	@Column()
	@QueryStringField()
	public long role_id;
	@Column()
	@QueryStringField()
	public long permission_id;
	@Column()
	@QueryStringField()
	public String permission;
	@Column()
	@QueryStringField()
	public String permission_code;
	
}
