package springless.permission;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import springless.db.ISimplePoJo;

@Entity(name = "role_permission")
public class RolePermission implements ISimplePoJo {
	static Logger logger = LoggerFactory.getLogger(RolePermission.class);
	@Id()
	@Column()
	@GeneratedValue()
	public long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column()
	//@GeneratedValue()
	public long permission_id;

	@Column()
	//@GeneratedValue()
	public long role_id;

}
