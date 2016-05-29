package test.controller;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import test.redis.CacheProxy;
import test.redis.ICacheDao;

public class MonitorServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8416728346192786168L;
	private ICacheDao cacheDaoImpl2 ;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//super.doGet(req, resp);
		//将key（随机），值，传来的本用户id放入第二个库
		String usName = req.getParameter("uname");
		if (usName != null && !"".equals(usName)){
			cacheDaoImpl2.save(UUID.randomUUID().toString(),
					usName, 160);
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		cacheDaoImpl2 = new CacheProxy(1);
	}
}
