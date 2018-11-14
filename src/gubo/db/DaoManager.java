package gubo.db;

import java.util.concurrent.ConcurrentHashMap;

public class DaoManager {
	private ConcurrentHashMap<Class<?>, SimplePoJoDAO> daoMap = new ConcurrentHashMap<Class<?>, SimplePoJoDAO>();

	public SimplePoJoDAO getDao(Class<?> clazz) {
		if (!this.daoMap.containsKey(clazz)) {
			SimplePoJoDAO dao = new SimplePoJoDAO(clazz);
			this.daoMap.put(clazz, dao);
		}
		return this.daoMap.get(clazz);
	}
}
