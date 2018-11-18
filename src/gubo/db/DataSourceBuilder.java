package gubo.db;

import java.util.Properties;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSourceBuilder {

	public DataSource build(Properties properties) {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(properties.getProperty("database.url"));
		config.setUsername(properties.getProperty("database.username"));
		config.setPassword(properties.getProperty("database.password"));
		HikariDataSource ds = new HikariDataSource(config);
		return ds;
	}
}
