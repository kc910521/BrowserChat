package test.controller;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;


@SuppressWarnings("deprecation")
public class WSocket extends WebSocketServlet {

	public static int USERNUMBER = 1;
	
	public static final Set<ChatWebSocket> users = new CopyOnWriteArraySet<ChatWebSocket>();
	/**
	 * 
	 */
	private static final long serialVersionUID = 6291048506162051622L;

	@Override
	protected StreamInbound createWebSocketInbound(String arg0,
			HttpServletRequest arg1) {
		// TODO Auto-generated method stub
		return new ChatWebSocket();
	}
	
}
