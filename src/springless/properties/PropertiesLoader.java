package springless.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class PropertiesLoader {
	Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);
    

	public Properties load(String... filepaths) {
		Properties ret = new Properties();
		for (String filepath : filepaths) {
			if (Strings.isNullOrEmpty(filepath)) {
				continue;
			}
			Properties prop = new Properties();
			InputStream input = null;
			try {
				input = new FileInputStream(filepath);
				// load a properties file
				prop.load(input);
				ret.putAll(prop);
			} catch (IOException ex) {
				logger.error("Not fatal, filepath: " + filepath , ex);
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return ret;
	}
}
