package test.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

import test.model.Msger;
import test.redis.CacheProxy;
import test.redis.ICacheDao;
import test.redis.impl.CacheDaoImpl;
import test.utils.MsgTools;

public class ChatWebSocket extends MessageInbound{

	
	private String username;
	
	private String userId;
	
    //private Set<ChatWebSocket> users = new CopyOnWriteArraySet<ChatWebSocket>();
    
    private ICacheDao cacheDaoImpl1 = new CacheProxy(0);
    
    private ICacheDao cacheDaoImpl2 = new CacheProxy(1);
    
    public ChatWebSocket() {
/*    	this.users.clear();
        this.users = null;
        this.users = users;*/
    }
    
    /**
     * 得到消息
     * @param data 得到的消息
     */
    @SuppressWarnings("deprecation")
	@OnMessage
    public void onMessage(String data) {
    	System.out.println("onMessage:"+data);
    	Msger msger = MsgTools.parseJsonToMsger(data);
        //String[] val1 = data.split("\\t");
    	if (msger != null){
    		if (msger.isInRegister()){
    			this.registerUser(msger);
    		}else {
    			this.pushMsg(msger,msger.getTo());
    			this.monitorMsg(msger);
    		}
    	}

    }

    private void registerUser(Msger msger){
        for(ChatWebSocket user : WSocket.users){
            if (user.userId.equals(msger.getRegId())){
                user.username=msger.getFrom();
            }
        }
    }
    
    /**
     * 根据消息接收者，发送信息
     * @param msger 消息体
     * @param msgGetter 消息接收者
     */
    @SuppressWarnings("deprecation")
	private void pushMsg(Msger msger,String msgGetter){
    	if (msgGetter != null && !"".equals(msgGetter)){
    		for(ChatWebSocket user : WSocket.users){
    			//查找当前所有存在实例，发送
    			if (user.username.equals(msgGetter)){
    				try {
    					CharBuffer temp = CharBuffer.wrap(msger.toString());
    					user.getWsOutbound().writeTextMessage(temp);
    				} catch (IOException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    					return;
    				}
    			}
    		}
    		//消息放入redis
    		cacheDaoImpl1.saveList(msger.getTo(), msger.toString(), 3600*2);
    	}else{
    		System.err.println("====  msgGetter is nil   ===");
    	}
    }
    
    /**
     * 将拿到的消息发送给有监听请求的用户
     * @param catchedMsg
     */
    private void monitorMsg(Msger msger){
    	//cacheDaoImpl2.loadHashBy(kName, k2Name);
    	String monitorName = null;
    	try {
    		monitorName = cacheDaoImpl2.randomGet();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if (monitorName != null){
    		this.pushMsg(msger, monitorName);
    	}
    	
    }
    
    /**
     * 开启连接后，向本实例再发一次请求(携带id的是否为注册请求到js脚本),去替换id名为用户姓名
     * 无法直接得到
     */
    @SuppressWarnings("deprecation")
	@Override
    protected void onOpen(WsOutbound outbound) {
    	// TODO Auto-generated method stub
        // this.connection=connection;WSocket.USERNUMBER
        this.userId = UUID.randomUUID().toString();//"#" + String.valueOf();
        WSocket.USERNUMBER ++;
        try {
            String message = MsgTools.parseObjectToJsonStr(new Msger(this.userId,true));
            CharBuffer buffer = CharBuffer.wrap(message);
            this.getWsOutbound().writeTextMessage(buffer);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        WSocket.users.add(this);
    }
    
    
    @Override
    protected void onClose(int status) {
    	// TODO Auto-generated method stub
    	WSocket.users.remove(this);
    	WSocket.USERNUMBER --;
    	super.onClose(status);
    }
    
	@Override
	protected void onBinaryMessage(ByteBuffer arg0) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("=====================================:"+arg0);
	}

	@Override
	protected void onTextMessage(CharBuffer arg0) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("=====================================:"+arg0);
		onMessage(arg0+"");
	}

}
