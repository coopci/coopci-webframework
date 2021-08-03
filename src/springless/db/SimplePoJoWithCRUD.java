package springless.db;

import java.sql.Connection;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.sql.DataSource;

import springless.reflection.FieldCopy;

public class SimplePoJoWithCRUD implements ISimplePoJo {

	static DaoManager daoManager = new DaoManager();
	@Id()
	@Column(name = "id")
	@GeneratedValue
	public Long id;
	
	@Override
	public void setId(long id) {
		this.id = id;
	}
	
	
	public void insert(Connection dbconn) throws Exception {
		SimplePoJoDAO dao = daoManager.getDao(this.getClass());
		dao.insert(dbconn, this);
	}


	public void insert(DataSource ds) throws Exception {
		try(Connection dbconn = ds.getConnection()) {
			SimplePoJoDAO dao = daoManager.getDao(this.getClass());
			dao.insert(dbconn, this);
		}
	}
	
	public void update(Connection dbconn) throws Exception {
		SimplePoJoDAO dao = daoManager.getDao(this.getClass());
		dao.update(dbconn, this);
	}
	
	/**
	 * 这个方法里会把 this 填上。 
	 * 
	 * @param dbconn
	 * @param pkName
	 * @param pkValue
	 * @return 是否从数据库里找到了符合条件的 数据。
	 * @throws Exception
	 */
	public boolean loadPoJoByPK(Connection dbconn, String pkName, Object pkValue) throws Exception {
		SimplePoJoDAO dao = daoManager.getDao(this.getClass());
		Object o = dao.loadPoJoByPK(dbconn, pkName, pkValue);
		if (o == null) {
			return false;
		}
		FieldCopy.copy(o, this);
		return true;
	}

}
