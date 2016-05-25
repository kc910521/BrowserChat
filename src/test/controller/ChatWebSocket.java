package test.controller;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

import test.redis.impl.CacheDaoImpl;

public class ChatWebSocket extends MessageInbound{

	
	private String username;
    private Set<ChatWebSocket> users = new CopyOnWriteArraySet<ChatWebSocket>();
    
    private CacheDaoImpl cacheDaoImpl = new CacheDaoImpl();
    
    public ChatWebSocket(Set<ChatWebSocket> users) {
    	this.users.clear();
        this.users = null;
        this.users = users;
    }
    
    @SuppressWarnings("deprecation")
	@OnMessage
    public void onMessage(String data) {
    	System.out.println("onMessage:"+data);
        String[] val1 = data.split("\\t");
        if(val1[0].equals("NAME")){
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
        }

    }

    
    @Override
    protected void onOpen(WsOutbound outbound) {
    	// TODO Auto-generated method stub
        // this.connection=connection;
        this.username = "#" + String.valueOf(WSocket.USERNUMBER);
        WSocket.USERNUMBER++;
        try {
            String message = "NAME" + "\t" + this.username;
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
