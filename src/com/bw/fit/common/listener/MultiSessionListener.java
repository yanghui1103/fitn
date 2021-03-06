package com.bw.fit.common.listener;

import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.bw.fit.common.model.LogUser;

public class MultiSessionListener implements HttpSessionListener,
		HttpSessionAttributeListener {
	ServletContext sc;
	List<LogUser> list = new ArrayList<>();
	// 新建一个session时触发此操作
	public void sessionCreated(HttpSessionEvent event) {
		sc = event.getSession().getServletContext();
		System.out.println("新建一个session");
	}

	// 销毁一个session时触发此操作
	public void sessionDestroyed(HttpSessionEvent se) {
		System.out.println("销毁一个session");
		if (!list.isEmpty()) {
			list.remove((LogUser) se.getSession().getAttribute("LogUser"));
			sc.setAttribute("onLineUserList", list);
		}
	}

	// 在session中添加对象时触发此操作，在list中添加一个对象
	public void attributeAdded(HttpSessionBindingEvent sbe) {	
		list.add((LogUser)sbe.getSession().getAttribute("LogUser")); 
		if(list!=null && list.size()>0){
			list.stream().map(x->x.getUser_cd()).forEach(str->System.out.println(str));
			List<String> ls = list.stream().map(x->x.getUser_cd()).distinct().collect(Collectors.toList());
			
			System.out.println("online person number:"+ls.size());	
		}
	}

	// 修改、删除session中添加对象时触发此操作
	public void attributeRemoved(HttpSessionBindingEvent arg0) {
	}

	public void attributeReplaced(HttpSessionBindingEvent arg0) {
	}
}