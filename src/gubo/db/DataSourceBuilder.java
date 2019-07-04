package gubo.db;

import java.lang.reflect.Proxy;
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
	public DataSource build(Properties properties, LeakTracker leakTracker) {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(properties.getProperty("database.url"));
		config.setUsername(properties.getProperty("database.username"));
		config.setPassword(properties.getProperty("database.password"));
		HikariDataSource ds = new HikariDataSource(config);
		if(leakTracker != null) {
	      ProxyDataSourceInvocationHandler h = new ProxyDataSourceInvocationHandler(ds, leakTracker);
	      DataSource ret = (DataSource) Proxy.newProxyInstance(DataSource.class.getClassLoader(),
	                new Class[] { DataSource.class },
	                h);
	      
	      return ret;
		} else {
		    return ds;
		}
	}
}
