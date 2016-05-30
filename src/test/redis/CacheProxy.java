package test.redis;

import java.io.UnsupportedEncodingException;
import java.util.List;

import test.redis.impl.CacheDaoImpl;

/**
 * redis库的代理redis方法
 * @author KCSTATION
 *
 */
public class CacheProxy implements ICacheDao{

	private static ICacheDao cacheDao = null;
	
	private int db_idx = 0;
	
	/**
	 * 
	 * @param dbIdx 数据库索引数，默认0开始
	 */
	public CacheProxy(int dbIdx){
		if (CacheProxy.cacheDao == null){
			CacheProxy.cacheDao = new CacheDaoImpl();
		}
		this.db_idx = dbIdx;
	}
	
	@Override
	public void saveHash(String k1Name, String k2Name, String valName,
			int liveSecond) {
		// TODO Auto-generated method stub
		cacheDao.setData_db(db_idx).saveHash(k1Name, k2Name, valName, liveSecond);
	}

	@Override
	public String loadHashBy(String kName, String k2Name)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return cacheDao.setData_db(db_idx).loadHashBy(kName, k2Name);
	}

	@Override
	public void saveList(String k1Name, String valName, int liveSecond) {
		// TODO Auto-generated method stub
		cacheDao.setData_db(db_idx).saveList(k1Name, valName, liveSecond);
	}

	@Override
	public List<String> loadListBy(String k1Name)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return cacheDao.setData_db(db_idx).loadListBy(k1Name);
	}

	@Override
	public ICacheDao setData_db(int data_db) {
		// TODO Auto-generated method stub
		return cacheDao.setData_db(db_idx).setData_db(data_db);
	}

	@Override
	public void save(String key, String val, int liveSecond) {
		// TODO Auto-generated method stub
		cacheDao.setData_db(db_idx).save(key, val, liveSecond);
	}

	@Override
	public String randomGet() throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return cacheDao.setData_db(db_idx).randomGet();
	}

}
