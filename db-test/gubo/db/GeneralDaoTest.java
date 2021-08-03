package gubo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import springless.db.DaoManager;
import springless.db.GeneralDao;
import springless.db.Person;
import springless.jdbc.mapping.ResultSetMapper;

public class GeneralDaoTest {
	HikariDataSource ds;
	@Before
	public void setup() {
		HikariConfig config = new HikariConfig();
        
        config.setJdbcUrl("jdbc:mysql://localhost:3308/test");
        config.setUsername("root");
        config.setPassword("123456");

        config.setMaximumPoolSize(10);
        config.setAutoCommit(false);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
         
        ds = new HikariDataSource(config);
	}
	
	@After
	public void teardown() {
		ds.close();
	}
	
	@Test
	public void testSelectAndUpdate() throws Exception {
		DaoManager daoManager = new DaoManager();
		GeneralDao generalDao = new GeneralDao(daoManager);
		
		
		try (Connection dbconn = ds.getConnection()) {
			dbconn.setAutoCommit(false);
			List<Person> personList = generalDao.loadPojoList(dbconn, Person.class, "select * from person limit 1");
			
			for (Person person : personList) {
				System.out.println("person.gender: " + person.gender);
				
			}
			
			
			Person person = personList.get(0);
			person.methods.add("m1");
			person.flipGender();
			generalDao.update(dbconn, person);
			dbconn.commit();
		}
		
		
	}
	
	@Test
	public void testLoadMapList() throws Exception {
		DaoManager daoManager = new DaoManager();
		GeneralDao generalDao = new GeneralDao(daoManager);
		
		
		try (Connection dbconn = ds.getConnection()) {
			dbconn.setAutoCommit(false);
			List<Map<String, Object>> personList = generalDao.loadMapList(dbconn, "select * from person limit 1");
			
			dbconn.commit();
		}
		
		
	}
}
