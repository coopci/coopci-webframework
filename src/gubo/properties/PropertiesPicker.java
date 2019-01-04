package gubo.properties;

import java.util.Map.Entry;
import java.util.Properties;

/**
 * Pick properties with a certain prefix out of a {@link java.util.Properties}
 * 
 **/
public class PropertiesPicker {
    static public Properties pick(String prefix, Properties origin) {
        Properties ret = new Properties();
        for (Entry<Object, Object> entry : origin.entrySet() ) {
            String k = entry.getKey().toString();
            if (k.startsWith(prefix)) {
                ret.put(entry.getKey(), entry.getValue());
            }
        }
        return ret;
    }
}
