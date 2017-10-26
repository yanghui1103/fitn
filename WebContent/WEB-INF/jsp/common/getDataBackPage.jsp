<%@page import="java.util.Date"%>
<%@ page contentType="text/event-stream; charset=UTF-8"%>
<%
	String s = request.getParameter("ds");
	System.out.println("tt2:"+s);
    response.setHeader("Content-Type", "text/event-stream");  //官方说法，一定要设置Content-Type为text/event-stream
    response.setHeader("Cache-Control", "no-cache");//官方说法，一定要设置Cache-Control为no-cache
    System.out.println("testtest");
    response.getWriter().write("data: >>server Time");  //官方说法，必须以data:开头 
    response.flushBuffer();
%>