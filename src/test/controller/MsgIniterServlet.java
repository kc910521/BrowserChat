package test.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import test.redis.impl.CacheDaoImpl;

public class MsgIniterServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7780956401325936181L;
	private CacheDaoImpl cacheDaoImpl = null;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String uname = req.getParameter("uname");
		List<String> alist = null;
		if (uname != null && !"".equals(uname)){
			//读取redis中，以本用户名为key的信息
			alist = cacheDaoImpl.loadListBy(uname);
		}
		resp.setCharacterEncoding("GBK");  
		resp.setContentType("text/html; charset=GBK"); 
		PrintWriter pw = resp.getWriter();
		pw.print(JSON.toJSONString(alist));
		pw.flush();
		pw.close();
	}
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		cacheDaoImpl = new CacheDaoImpl();
		super.init();
	}
	
}
