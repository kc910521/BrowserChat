package test.redis;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * REDIS �ӿ�
 * @author KCSTATION
 *
 */
public interface ICacheDao {

	//�洢map
	public void saveHash(String k1Name,String k2Name,String valName,int liveSecond);
	//��ȡmap
	public String loadHashBy(String kName,String k2Name) throws UnsupportedEncodingException;
	//�洢list
	public void saveList(String k1Name, String valName,
			int liveSecond);
	
	/**
	 * ��ȡlist������redis��list
	 * @param k1Name
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public List<String> loadListBy(String k1Name)
			throws UnsupportedEncodingException;
}
