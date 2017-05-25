package com.bw.fit.system.service;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.bw.fit.common.model.LogUser;

public interface SystemService {

	/****
	 * @author yangh
	 * @result 
	 */
	public JSONObject getOnLineSituation(HttpSession session,LogUser user,ServletContext servletContext);
}
