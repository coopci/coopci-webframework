package spingless.jdbc;

import java.util.Collection;
import java.util.Set;

public class Utils {

	/**
	   *  因为mysql的driver不支持 createArrayOf ，所以才需要这个。
	 *	把逗号分割单词转成sql数组。
	 *	@param words  逗号分割的单词。
	 **/
	public static String wordsToSqlArray(String words) {
		String[] wordsArray = words.split(",");
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		for (int i = 0; i < wordsArray.length; ++i) {
			sb.append('\'');
			sb.append(wordsArray[i]);
			sb.append('\'');
			if (i < wordsArray.length - 1) {
				sb.append(", ");
			}
		}
		sb.append(')');
		return sb.toString();
	}
	
	
	public static String wordsToSqlArray(Collection<String> words) {
		
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		int i = 0;
		for (String w : words) {
			sb.append('\'');
			sb.append(w);
			sb.append('\'');
			if (i < words.size() - 1) {
				sb.append(", ");
			}
			++i;
		}
		sb.append(')');
		return sb.toString();
	}
	
	public static String toSqlArray(Set<Long> longs) {
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		int i = 0;
		for (Long n : longs) {
			sb.append(n);
			if (i < longs.size() - 1) {
				sb.append(", ");
			}
			++i;
		}
		sb.append(')');
		return sb.toString();
	}
}
