package coopci.sitemail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

public class JdbcSitemailService implements SitemailService {

	public static class DBUnavailableException extends Exception
	{
		public DBUnavailableException(String msg) {
			super(msg);
		}
	}
	private final DataSource ds;
	private final DataSource roDs;
	
	
	private Connection getConnection() throws SQLException {
		if(this.ds !=null) {
			return ds.getConnection();
		}
		throw new IllegalStateException("master DB unavailable");
		
	}
	

	private Connection getRoConnection() throws SQLException {
		if(this.roDs !=null) {
			return this.roDs.getConnection();
		}
		
		if(this.ds !=null) {
			return this.ds.getConnection();
		}
		
		throw new IllegalStateException("Both DB and RO DB unavailable");
		
	}
	public JdbcSitemailService(DataSource ds, DataSource roDs){
		this.ds = ds;
		this.roDs = roDs;
	} 
	{}
	@Override
	public List<MailDigest> list(String receiver, String lang, int pnum, int psize) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<MailDigest> listSent(String sender, String lang, int pnum, int psize) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public MailBody getMailBody(String user, String id, String lang) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String compose(String author, String lang, MailBody body) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void update(String id, String lang, MailBody body) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void send(String id, String sid, String... rid) {
		// TODO Auto-generated method stub
		
	}
}
