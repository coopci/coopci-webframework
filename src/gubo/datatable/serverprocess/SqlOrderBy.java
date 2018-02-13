package gubo.datatable.serverprocess;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.glassfish.grizzly.http.server.Request;

public class SqlOrderBy {

	public static String toSqlOrderByMultivalue(Request request, String[] columnnames, String dialect) {
		Map<String, String> p = new HashMap<String, String>();
		for (Entry<String, String[]> entry: request.getParameterMap().entrySet()) {
			if (entry.getValue().length == 0)
				continue;
			p.put(entry.getKey(), entry.getValue()[0]);
		}
		return toSqlOrderBy(p, columnnames, dialect);
	}
	public static String toSqlOrderBy(Map<String, String> dtParams, String[] columnnames, String dialect) {
		
		StringBuilder sb = new StringBuilder();
		String sep = " order by ";
		for (int i = 0; i < columnnames.length; ++i) {
			String k = String.format("order[%d][column]", i);
			if (dtParams.containsKey(k)) {
				int columIndex = Integer.parseInt(dtParams.get(k));
				
				String keyDir = String.format("order[{}][dir]", i);
				String columDir = "asc";
				
				if (dtParams.containsKey(keyDir)) {
					columDir = dtParams.get(keyDir);	
				}
				sb.append(String.format(" %s %s %s", sep, columnnames[columIndex], columDir));
//				sb.append(sep);
//				sb.append(columnnames[columIndex]);
//				sb.append(" ");
//				sb.append(columDir);
				
				sep = " , ";
			}
		}
		
		return sb.toString();
	}
}
