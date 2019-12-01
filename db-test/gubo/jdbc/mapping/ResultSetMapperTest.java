package gubo.jdbc.mapping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import gubo.db.Person;


//CREATE TABLE `person` (
//		   `id` bigint(20) NOT NULL AUTO_INCREMENT,
//		   `name` varchar(45) DEFAULT NULL,
//		   `age` int(11) DEFAULT NULL,
//		   `salary` decimal(10,2) DEFAULT NULL,
//		   `methods` set('m1','m2') DEFAULT NULL,
//		   `gender` varchar(45) NOT NULL DEFAULT '',
//		   PRIMARY KEY (`id`),
//		   KEY `methods` (`methods`)
//		 ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
//		 
public class ResultSetMapperTest {
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
	public void testMapRersultSetToObject() throws SQLException {
		ResultSetMapper<Person> mapper = new ResultSetMapper<Person>();
		
		try (Connection dbconn = ds.getConnection()) {
			PreparedStatement stmt = dbconn.prepareStatement("select * from person limit 1");
			
			ResultSet rs = stmt.executeQuery();
			List<Person> personList = mapper.mapRersultSetToObject(rs, Person.class);
			
			for (Person person : personList) {
				System.out.println("person.gender: " + person.gender);
				
			}
		}
		
		
	}
}
