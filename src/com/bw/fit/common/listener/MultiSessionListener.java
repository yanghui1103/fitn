package com.bw.fit.common.listener;

import java.util.HashMap;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.bw.fit.common.model.LogUser;

public class MultiSessionListener implements HttpSessionListener {
	private static HashMap hUserName = new HashMap();//保存sessionID和username的映射
	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
		hUserName.put(arg0.getSession().getId(), ((LogUser)arg0.getSession().getAttribute("LogUser")).getUser_cd());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
		hUserName.remove(arg0.getSession().getId());
	}


}
