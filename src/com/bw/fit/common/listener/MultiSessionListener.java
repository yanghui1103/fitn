package com.bw.fit.common.listener;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bw.fit.common.model.LogUser;

public class MultiSessionListener extends HttpServlet implements HttpSessionListener {
	private static HashMap hUserName = new HashMap();//保存sessionID和username的映射  WebApplicationContext
	private ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext()); 
	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub 
		// hUserName.put(arg0.getSession().getId(), ((LogUser)arg0.getSession().getAttribute("LogUser")).getUser_cd());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
		hUserName.remove(arg0.getSession().getId());
	}


}
