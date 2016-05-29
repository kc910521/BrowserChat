package test.redis.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;



import redis.clients.jedis.Jedis;
import test.redis.ICacheDao;
import test.redis.RedisUtils;

public class CacheDaoImpl implements ICacheDao{

	//信息发送数据库
	private int data_db = 0;//监控需求库：monitor_db
	
	@Override
	public void saveHash(String k1Name,String k2Name,String valName, int liveSecond) {
		// TODO Auto-generated method stub
		
		if (k1Name == null || "".equals(k1Name) 
				||k2Name == null || "".equals(k2Name)
				||valName == null || "".equals(valName) ){
			System.err.println("ERROR PARAMETERS HERE!");
			return;
		}
		Jedis jds = RedisUtils.getJedis(data_db);
		jds.hset(k1Name.getBytes(), k2Name.getBytes(), valName.getBytes());
		if (liveSecond > 0){
			jds.expire(k1Name.getBytes(),liveSecond);
		}
		RedisUtils.returnResource(jds);
	}

	@Override
	public String loadHashBy(String k1Name,String k2Name) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		if (k1Name == null || "".equals(k1Name) 
				||k2Name == null || "".equals(k2Name) ){
			System.err.println("ERROR PARAMETERS HERE!");
			return null;
		}
		Jedis jds = RedisUtils.getJedis(data_db);
		byte[] btStr = jds.hget(k1Name.getBytes(), k2Name.getBytes());
		RedisUtils.returnResource(jds);
		return new String(btStr,"gbk");
		
	}

	public static void main(String[] args) {
		CacheDaoImpl cacheDaoImpl = new CacheDaoImpl();
//		cacheDaoImpl.saveList("张", "艺术1", 1600);
//		cacheDaoImpl.saveList("张", "艺术2", 1600);
//		cacheDaoImpl.saveList("张", "艺术3", 1600);
		try {
			System.out.println(cacheDaoImpl.loadListBy("张").toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void saveList(String k1Name, String valName,
			int liveSecond) {
		// TODO Auto-generated method stub
		if (k1Name == null || "".equals(k1Name) 
				||valName == null || "".equals(valName) ){
			System.err.println("ERROR PARAMETERS HERE!");
			return;
		}
		Jedis jds = RedisUtils.getJedis(data_db);
		jds.lpush(k1Name.getBytes(), valName.getBytes());
		if (liveSecond > 0){
			jds.expire(k1Name.getBytes(),liveSecond);
		}
		RedisUtils.returnResource(jds);
	}

	@Override
	public List<String> loadListBy(String k1Name)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		if (k1Name == null || "".equals(k1Name) ){
			System.err.println("ERROR PARAMETERS HERE!");
			return null;
		}
		List<String> msgList = new ArrayList<>();
		Jedis jds = RedisUtils.getJedis(data_db);
		byte[] btStr = null;
		for (int ai = jds.llen(k1Name.getBytes()).intValue() ; ai > 0 ;ai --){
			btStr = jds.lpop(k1Name.getBytes());
			if (btStr != null && btStr.length > 0){
				msgList.add(new String(btStr,"gbk"));
			}
		}
		RedisUtils.returnResource(jds);
		return msgList;
	}

	@Override
	public ICacheDao setData_db(int data_db) {
		this.data_db = data_db;
		return this;
	}

	@Override
	public void save(String key, String val, int liveSecond) {
		// TODO Auto-generated method stub
		if (key == null || "".equals(key) 
				||val == null || "".equals(val) ){
			System.err.println("ERROR PARAMETERS HERE!");
			return;
		}
		Jedis jds = RedisUtils.getJedis(data_db);
		jds.set(key.getBytes(), val.getBytes());
		if (liveSecond > 0){
			jds.expire(key.getBytes(),liveSecond);
		}
		RedisUtils.returnResource(jds);
	}

	@Override
	public String randomGet() throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		Jedis jds = RedisUtils.getJedis(data_db);
		byte[] btStr = jds.randomBinaryKey();
		if (btStr == null || btStr.length <= 0){
			return null;
		}
		byte[] btStrVal = jds.get(btStr);
		RedisUtils.returnResource(jds);
		return new String(btStrVal,"gbk");
	}
	
	
	
}
