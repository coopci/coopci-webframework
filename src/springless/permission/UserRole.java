package springless.permission;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import springless.db.ISimplePoJo;

@Entity(name = "user_role")
public class UserRole implements ISimplePoJo {
	@Id()
	@Column()
	@GeneratedValue()
	public long id;
	@Column()
	public long user_id;
	@Column()
	public long role_id;
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
