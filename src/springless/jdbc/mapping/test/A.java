package springless.jdbc.mapping.test;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import springless.http.querystring.QueryStringBinder;
import springless.http.querystring.QueryStringField;


/**
 * 
 *
 * CREATE TABLE `table1` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT,
   `c2` bigint(20) NOT NULL DEFAULT 8,
   `c3` varchar(45) DEFAULT NULL,
   `ctime` datetime DEFAULT NULL,
   `cboolean` tinyint(4) DEFAULT 0,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
 * 
 * 
 * */
@Entity(name="table1")
public class A {
	
	@Id()
	@Column()
	@GeneratedValue()
	@QueryStringField
	public long id;
	
	@Column()
	public long c2;
	

	@Column()
	public String c3;

	@Column(updatable = false)
	public Date ctime = new Date();

	@Column()
	@QueryStringField
	public Boolean cboolean = true;
	
	@Override
	public String toString() {
		
		QueryStringBinder binder = new QueryStringBinder();
		String ret;
		try {
			ret = binder.toQueryString(this, null);
			return ret;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static void print(List<A> pojolist) {
		
		for (A a : pojolist) {
			System.out.println(a);
			
		}
	}
}
