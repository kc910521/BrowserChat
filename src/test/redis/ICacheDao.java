package test.redis;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * REDIS 接口
 * @author KCSTATION
 *
 */
public interface ICacheDao {

	//存储map
	public void saveHash(String k1Name,String k2Name,String valName,int liveSecond);
	//读取map
	public String loadHashBy(String kName,String k2Name) throws UnsupportedEncodingException;
	//存储list
	public void saveList(String k1Name, String valName,
			int liveSecond);
	
	/**
	 * 读取list并销毁redis中list
	 * @param k1Name
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public List<String> loadListBy(String k1Name)
			throws UnsupportedEncodingException;
}
