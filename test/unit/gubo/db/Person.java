package gubo.db;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "person")
@Table(name = "person")
public class Person {

	
	@Column
	public long id;
	
	@Column
	public String name;
	
	@Column
	public int age;
	
	@Column
	public BigDecimal salary;
}
