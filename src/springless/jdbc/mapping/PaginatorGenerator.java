package springless.jdbc.mapping;

import java.sql.Connection;
import java.util.Map;

/**
 *	用来生成  limit offset 语句。 
 * 
 **/
public class PaginatorGenerator {

	/**
	 * 目前这个只会输出mysql语法的。
	 * @param dbconn 可能需要从这个参数判断数据库语法。
	 * @param data  需要同时包含 page_size 和 page_num，否则返回  ""; page_num 从 1开始
	 **/
	public String generate(Connection dbconn, Map<String, String> data) {
		try {
			int pageSize = Integer.parseInt(data.get("page_size"));
			int pageNum = Integer.parseInt(data.get("page_num"));
			if (pageSize < 0 || pageNum < 1) {
				return "";
			}
			
			int start = (pageNum-1) * pageSize;
			return " LIMIT " + start + ", " + pageSize;
		} catch (Exception ex) {
			return "";
		}
	}
}
