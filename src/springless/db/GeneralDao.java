package springless.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import springless.jdbc.mapping.ResultSetMapper;

/**
 *	根据要操作的pojo自动选择要使用哪个 SimplePoJoDAO。
 **/
public class GeneralDao {

	private DaoManager daoManager;
	public GeneralDao(DaoManager daoManager) {
		this.daoManager = daoManager;
	}
	public GeneralDao() {
		this.daoManager = new DaoManager();
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
	
	public <T extends ISimplePoJo> T insert(Connection dbconn, T pojo)
			throws Exception {
		SimplePoJoDAO simplePoJoDAO = this.getSimplePoJoDAO(pojo);
		T ret = simplePoJoDAO.insert(dbconn, pojo);
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
	
	public <T> T loadPojoByPk(DataSource ds, Class<?> clazz, String pkName, Object pkValue) throws SQLException {
		
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
	
	public <T> T loadPojo(Connection dbconn, Class<?> clazz, String sql,
			Object... params) throws SQLException {
		
		SimplePoJoDAO simplePoJoDAO = this.daoManager.getDao(clazz);
		T ret = simplePoJoDAO.loadPojo(dbconn, sql, params);
		return ret ;
	}
	
	public <T> List<T> loadPojoList(DataSource ds, Class<?> clazz, String sql,
			Object... params) throws SQLException {
		
		SimplePoJoDAO simplePoJoDAO = this.daoManager.getDao(clazz);
		List<T> ret = simplePoJoDAO.loadPojoList(ds, sql, params);
		return ret ;
	}
	
	public List<Map<String, Object>> loadMapList(Connection dbconn, String sql,
			Object... params) throws SQLException {
		// 其实这里 ResultSetMapper 的模板参数随便填什么都行。
		ResultSetMapper<Map<String, Object>> mapper = new ResultSetMapper<Map<String, Object>>();
		List<Map<String, Object>> pojoList = mapper.loadMapList(dbconn, sql, params);
		return pojoList;
	}
	
	public List<Map<String, Object>> loadMapList(DataSource ds, String sql,
			Object... params) throws SQLException {
		List<Map<String, Object>> ret = null;
		try(Connection dbconn = ds.getConnection()) {
			dbconn.setAutoCommit(false);
			ret = this.loadMapList( dbconn,  sql,params);
			dbconn.commit();
		}
		return ret;
	}
	

	public <T> T loadPoJoByUniqueKeys(Connection dbconn, Class<?> clazz, Object ... keyNamesAndvalues) throws SQLException {
		
		SimplePoJoDAO simplePoJoDAO = this.daoManager.getDao(clazz);
		T ret = simplePoJoDAO.loadPoJoByUniqueKeys(dbconn, keyNamesAndvalues);
		return ret ;
	}
}
