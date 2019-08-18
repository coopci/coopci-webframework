package gubo.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

/**
 *	根据要操作的pojo自动选择要使用哪个 SimplePoJoDAO。
 **/
public class GeneralDao {

	private DaoManager daoManager;
	public GeneralDao(DaoManager daoManager) {
		this.daoManager = daoManager;
	}
	
	SimplePoJoDAO getSimplePoJoDAO(Object pojo) {
		Class<?> clazz = pojo.getClass();
		return this.daoManager.getDao(clazz);
	}
	public <T extends ISimplePoJo> T insert(DataSource ds, T pojo)
			throws Exception {
		SimplePoJoDAO simplePoJoDAO = this.getSimplePoJoDAO(pojo);
		T ret = simplePoJoDAO.insert(ds, pojo);
		return ret;
	}
	
	public <T extends ISimplePoJo> T update(DataSource ds, T pojo)
			throws Exception {
		SimplePoJoDAO simplePoJoDAO = this.getSimplePoJoDAO(pojo);
		T ret = simplePoJoDAO.update(ds, pojo);
		return ret;
	}
	
	public <T extends ISimplePoJo> T update(Connection dbconn, T pojo)
			throws Exception {
		SimplePoJoDAO simplePoJoDAO = this.getSimplePoJoDAO(pojo);
		T ret = simplePoJoDAO.update(dbconn, pojo);
		return ret;
	}
	
	public <T extends ISimplePoJo> T update(DataSource ds, T pojo, String ... allowedCols)
			throws Exception {
		SimplePoJoDAO simplePoJoDAO = this.getSimplePoJoDAO(pojo);
		T ret = simplePoJoDAO.update(ds, pojo, allowedCols);
		return ret;
	}
	
	public <T extends ISimplePoJo> T update(Connection dbconn, T pojo, String ... allowedCols)
			throws Exception {
		SimplePoJoDAO simplePoJoDAO = this.getSimplePoJoDAO(pojo);
		T ret = simplePoJoDAO.update(dbconn, pojo, allowedCols);
		return ret;
	}
	
	public <T> T loadPojoByPk(Connection dbconn, Class<?> clazz, String pkName, Object pkValue) throws SQLException {
		
		SimplePoJoDAO simplePoJoDAO = this.daoManager.getDao(clazz);
		T ret = simplePoJoDAO.loadPoJoByPK(dbconn, pkName, pkValue);
		return ret ;
	}
	
	public <T> T loadPojoByPk(DataSource ds, Class<?> clazz, String sql, String pkName, Object pkValue) throws SQLException {
		
		SimplePoJoDAO simplePoJoDAO = this.daoManager.getDao(clazz);
		T ret = simplePoJoDAO.loadPoJoByPK(ds, pkName, pkValue);
		return ret ;
	}
	
	public <T> List<T> loadPojoList(Connection dbconn, Class<?> clazz, String sql,
			Object... params) throws SQLException {
		
		SimplePoJoDAO simplePoJoDAO = this.daoManager.getDao(clazz);
		List<T> ret = simplePoJoDAO.loadPojoList(dbconn, sql, params);
		return ret ;
	}
	
	public <T> List<T> loadPojoList(DataSource ds, Class<?> clazz, String sql,
			Object... params) throws SQLException {
		
		SimplePoJoDAO simplePoJoDAO = this.daoManager.getDao(clazz);
		List<T> ret = simplePoJoDAO.loadPojoList(ds, sql, params);
		return ret ;
	}
	
	
}
