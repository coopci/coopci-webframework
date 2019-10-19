package gubo.jdbc;

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
}
