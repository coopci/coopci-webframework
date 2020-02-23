package gubo.properties;

import java.util.Map.Entry;
import java.util.Properties;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkArgument;
/**
 * Pick properties with a certain prefix out of a {@link java.util.Properties}
 * 
 **/
public class PropertiesPicker {
    static public Properties pick(String prefix, Properties origin) {
        checkNotNull(prefix);
        checkNotNull(origin);
        checkArgument(prefix.length() > 0, "prefix must contains one or more characters.");
        Properties ret = new Properties();
        for (Entry<Object, Object> entry : origin.entrySet() ) {
            String k = entry.getKey().toString();
            if (k.startsWith(prefix)) {
            	
            	
                ret.put(k.substring(prefix.length()), entry.getValue());
            }
        }
        return ret;
    }
}
