package coopci.sitemail;

import java.util.List;

/**
 * 站内信模块的功能 定义 
 **/
public interface SitemailService {

	/**
	 *	获取信件列表 
	 * @param receiver 收信人id。不能是null或空。
	 * @param lang  语言。不能是null或空
	 * @param pnum 页码  必须>=0
	 * @param psize 页大小 ，必须大于0
	 **/
	List<MailDigest> list(String receiver, String lang,int pnum,
			int psize
			);
	
	/**
	 *	获取已发信列表 
	 * @param receiver 发信人id。不能是null或空。
	 * @param lang  语言。不能是null或空
	 * @param pnum 页码  必须>=0
	 * @param psize 页大小 ，必须大于0
	 **/
	List<MailDigest> listSent(String sender, String lang,int pnum,
			int psize
			);
	
	/**
	 *	获取信内容
	 * @param user 必须是发信人id或守信人id，否则无权访问,不能是null或空。
	 * @param lang  语言。不能是null或空。
	 * @param id 信的id。
	 **/
	MailBody getMailBody(String user, String id, String lang);
	
	/**
	 * 写信
	 * 
	 * @return  信 id。
	 * 
	 * */
	String compose(String author,String lang, MailBody body );
	
	/**
	 * 改信
	 * 发送前和发送后都可以改。
	 * @param id 信id
	 * @param lang  语言。不能是null或空。
	 * @param body 改后的内容。
	 * */
	void update(String id ,String lang, MailBody body);
	
	/****
	 * 发信
	 * 
	 * @param id 信id
	 * @param sid 发信人id
	 * @pram rid 收信人id
	 * */
	void send(String id, String sid, String ... rid);
	
}
