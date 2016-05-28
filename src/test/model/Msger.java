package test.model;

import com.alibaba.fastjson.JSONObject;


/**
 * ��ϢVO
 * @author KCSTATION
 *
 */
public class Msger {

	//ע��ʱ�õ�id
	private String regId;
	//��Ϣ��Դ
	private String from;
	//��ϢĿ��
	private String to;
	//��Ϣ����
	private String content;
	//��һ�ν���
	private boolean inRegister = false;

	
	public Msger(){}
	/**
	 * default msger
	 * @param from
	 * @param to
	 * @param content
	 */
	public Msger(String from, String to, String content) {
		super();
		this.from = from;
		this.to = to;
		this.content = content;
	}

	

	public Msger(String from, String to, String content, boolean inRegister) {
		super();
		this.from = from;
		this.to = to;
		this.content = content;
		this.inRegister = inRegister;
	}
	
	


	/**
	 * ע���� ����
	 * @param regId
	 * @param from
	 * @param to
	 * @param content
	 * @param inRegister ע��ʱ����Ϊtrue
	 */
	public Msger(String regId, 
			boolean inRegister) {
		super();
		this.regId = regId;
		this.inRegister = inRegister;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{"
				+ "\"regId\":\""+regId+"\","
				+ "\"from\":\""+from+"\","
				+ "\"to\":\""+to+"\","
				+ "\"content\":\""+content+"\","
				+ "\"inRegister\":\""+inRegister+"\""
				+ "}";
	}
	
	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isInRegister() {
		return inRegister;
	}

	public void setInRegister(boolean inRegister) {
		this.inRegister = inRegister;
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	
	
	
}
