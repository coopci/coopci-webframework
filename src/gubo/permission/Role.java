package gubo.permission;

import gubo.db.ISimplePoJo;
import gubo.http.querystring.QueryStringField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity(name = "role")
public class Role implements ISimplePoJo {

	public Role(){}
	
	public Role(String n) {
		this.name = n;
	}
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

	@Column(name = "name")
	@QueryStringField()
	public String name;
	
}
