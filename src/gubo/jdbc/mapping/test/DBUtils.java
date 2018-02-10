package gubo.jdbc.mapping.test;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBUtils {

//	public static String dbUrl = "";
//	public static String dbUser = "";
//	public static String dbPassword = "";
	
	public static String dbUrl = "jdbc:mysql://localhost:3308/mobtrade?useUnicode=true&characterEncoding=UTF-8";
	public static String dbUser = "mobtrade";
	public static String dbPassword = "mobtrade";
	
	
	public static HikariDataSource makeNewHikariDataSource() {
		HikariConfig config = new HikariConfig();
    	config.setJdbcUrl(dbUrl);
    	config.setUsername(dbUser);
    	config.setPassword(dbPassword);
//    	config.addDataSourceProperty("cachePrepStmts", "true");
//    	config.addDataSourceProperty("prepStmtCacheSize", "250");
//    	config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

    	HikariDataSource ds = new HikariDataSource(config);
    	return ds;
	}
	
	static HikariDataSource ds = null;
	public static Connection getHikariConnection () throws SQLException {
		if (ds == null) {
			ds = makeNewHikariDataSource();
		}
		return ds.getConnection();
	}
	
	
}

