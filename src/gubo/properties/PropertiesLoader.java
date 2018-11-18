package gubo.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

	public Properties load(String... filepaths) {
		Properties ret = new Properties();
		for (String filepath : filepaths) {
			Properties prop = new Properties();
			InputStream input = null;
			try {
				input = new FileInputStream(filepath);
				// load a properties file
				prop.load(input);
				ret.putAll(prop);
			} catch (IOException ex) {
				ex.printStackTrace();
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
