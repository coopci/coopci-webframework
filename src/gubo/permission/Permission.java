package gubo.permission;

import gubo.db.ISimplePoJo;
import gubo.http.querystring.QueryStringField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Entity(name = "permission")
public class Permission implements ISimplePoJo {
	protected static Logger logger = LoggerFactory.getLogger(Permission.class);
	
	public Permission(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public Permission(String code, String name, String desc, String adminPages) {
		this.code = code;
		this.name = name;
		this.desc = desc;
	}

	public Permission() {}

	@Id()
	@Column()
	@GeneratedValue()
	public long id;

	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "name")
	@QueryStringField()
	public String name;

	@Column(name = "code")
	@QueryStringField()
	public String code;
	
	/**
	 * 权限描述。
	 */
	@Column()
	public String desc="";
	
}
