package gubo.db;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import gubo.properties.PropertiesPicker;

/**
 *	用来维护多个DataSource，用在读写分离或者 sharding的情况下。 
 * 
 **/
public class DataSourceProvider {
	Logger logger = LoggerFactory.getLogger(getClass());
	// key 是name。
	ConcurrentHashMap<String, DataSource> dataSources = new ConcurrentHashMap<String, DataSource>(); 
	
	public void build(Properties properties, String ... names  ) {
		for (String name : names) {
			
			Properties props = PropertiesPicker.pick(name + ".", properties);
			if(props.size() == 0) {
				logger.error("No properties for datasource " + name);
			}
			HikariConfig config = new HikariConfig(props);
			
			HikariDataSource ds = new HikariDataSource(config);
			
			this.dataSources.put(name, ds);
		}
	}
	public DataSource getDataSource() {
		return this.getDataSource("default");
	}
	
	public DataSource getDataSource(String name) {
		return this.dataSources.get(name);
	}
	
}
