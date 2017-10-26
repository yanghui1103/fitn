package com.bw.fit.common.util;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.impl.json.JsonObjectConverter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bw.fit.common.dao.CommonDao;
import com.bw.fit.common.model.CommonModel;
import com.bw.fit.common.model.LogUser;

public class ImmediateMessageUtil {
	/***
	 * 即时通讯
	 */
	public static String getMessageInfo(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		if (request.getSession(false) == null) {
			json.put("res", "1");
			json.put("msg", "会话失效,请重新登录");
			return json.toJSONString() ;
		}
		LogUser user = ((LogUser) request.getSession(false).getAttribute(
				"LogUser"));
		// httpClient
		HttpClient httpClient = new DefaultHttpClient();
		// get method
		HttpGet httpGet = new HttpGet(
				PropertiesUtil.getValueByKey("system.default.url")
						+ "system/getMessageInteractiveInfo" + "/" + user.getUser_id());
		// response
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
		} catch (Exception e) {
		}
		// get response into String
		String temp = "";
		try {
			HttpEntity entity = response.getEntity();
			temp = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
		} 
		return temp;
	}
}
