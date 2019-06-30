package gubo.db;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "person")
@Table(name = "person")
public class Person implements ISimplePoJo{

	@Id
	@Column
	@GeneratedValue
	public long id;
	
	@Column
	public String name;
	
	@Column
	public int age;
	
	@Column
	public BigDecimal salary;
	
	@Column
	public Set<String> methods;

	@Override
	public void setId(long id) {
		this.id = id;
	}
}
