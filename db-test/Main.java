import gubo.db.DaoManager;
import gubo.db.Person;
import gubo.db.SimplePoJoDAO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Arrays;

import javax.sql.DataSource;

import com.google.common.collect.Sets;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


public class Main {

	public static DataSource build() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:mysql://localhost:3308/test");
		config.setUsername("root");
		config.setPassword("123456");
		HikariDataSource ds = new HikariDataSource(config);
		return ds;
	}
	
	public static void insert() throws Exception {
		DaoManager daoManager = new DaoManager();
		SimplePoJoDAO personDao = daoManager.getDao(Person.class);
		DataSource ds = build();
		try(Connection dbconn = ds.getConnection()) {
			Person person = new Person();
			person.name = "John";
			person.age = 20;
			person.salary = new BigDecimal("10000.00");
			person.methods = Sets.newConcurrentHashSet(Arrays.asList("m1","m2"));
			personDao.insert(dbconn, person);
		}
	}
	public static void update() throws Exception {
		DaoManager daoManager = new DaoManager();
		SimplePoJoDAO personDao = daoManager.getDao(Person.class);
		DataSource ds = build();
		try(Connection dbconn = ds.getConnection()) {
			Person person = personDao.loadPoJoByPK(dbconn, "id", 1);
			person.methods.remove("m1");
			personDao.update(dbconn, person);
		}
	}
	public static void main(String[] args) throws Exception {
		// insert();
		update();
	}
}
