package coopci.sitemail;
import java.util.Date;

public class MailDigest {

	//已经标记为已读。
	public boolean markedRead=false;
	
	public String title="";
	
	//获取信体时用的id。
	public String id;
	
	// 收信时间
	public Date rtime;
}
