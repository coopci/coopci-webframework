package springless.exceptions;

/**
 *	表示多个 线程同时更改同一个数据，导致当前线程更改失败。
 *  一般重试就可以成功。
 * 
 **/
public class ContentionException extends ApiException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5025742396477189364L;
	
	public ContentionException(String msg) {
		super(msg);
	}
	public ContentionException() {
		super();
	}
}
