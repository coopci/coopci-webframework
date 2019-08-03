package gubo.db;

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
}
