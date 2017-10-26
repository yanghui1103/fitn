<%@page import="java.util.Date,com.bw.fit.common.util.*,org.json.simple.JSONObject"%>
<%@ page contentType="text/event-stream; charset=UTF-8"%>
<%	
    response.setHeader("Content-Type", "text/event-stream");  //官方说法，一定要设置Content-Type为text/event-stream
    response.setHeader("Cache-Control", "no-cache");//官方说法，一定要设置Cache-Control为no-cache
    String json = ImmediateMessageUtil.getMessageInfo(request);
    response.getWriter().write("data:" + json );  //官方说法，必须以data:开头 
    response.flushBuffer();
%>