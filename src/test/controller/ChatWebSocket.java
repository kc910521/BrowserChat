package test.controller;

import java.io.IOException;
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
import test.redis.impl.CacheDaoImpl;
import test.utils.MsgTools;

public class ChatWebSocket extends MessageInbound{

	
	private String username;
	
	private String userId;
	
    private Set<ChatWebSocket> users = new CopyOnWriteArraySet<ChatWebSocket>();
    
    private CacheDaoImpl cacheDaoImpl = new CacheDaoImpl();
    
    public ChatWebSocket(Set<ChatWebSocket> users) {
    	this.users.clear();
        this.users = null;
        this.users = users;
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
    			this.pushMsg(msger);
    		}
    	}
    	//:NAME	#1_是
/*        if(val1[0].equals("NAME")){
            String[] val2=val1[1].split("_");
            for(ChatWebSocket user:users){
                if (user.username.equals(val2[0])){
                    user.username=val2[1];
                }
            }
        }else if(val1[0].equals("MSG")){
            String[] val2=val1[1].split("_");
            //boolean targetOnline = false;
            for(ChatWebSocket user : users){
            	//查找当前所有存在实例，发送
                if (user.username.equals(val2[1])){
                    try {
                        CharBuffer temp = CharBuffer.wrap(data);
                        user.getWsOutbound().writeTextMessage(temp);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        return;
                    }
                }
            }     
            cacheDaoImpl.saveList(val2[1], val2[0]+" 对你说："+val2[2], 3600);
        }
        else{
            System.out.println("ERROR");
        }*/

    }

    private void registerUser(Msger msger){
        for(ChatWebSocket user:users){
            if (user.userId.equals(msger.getRegId())){
                user.username=msger.getFrom();
            }
        }
    }
    
    //根据消息发送信息
    @SuppressWarnings("deprecation")
	private void pushMsg(Msger msger){
    	System.out.println("发送消息需要："+msger);
        for(ChatWebSocket user : users){
        	//查找当前所有存在实例，发送
            if (user.username.equals(msger.getTo())){
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
        cacheDaoImpl.saveList(msger.getTo(), msger.toString(), 3600*2);
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
        users.add(this);
    }
    
    
    @Override
    protected void onClose(int status) {
    	// TODO Auto-generated method stub
    	users.remove(this);
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
