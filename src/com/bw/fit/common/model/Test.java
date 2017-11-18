package com.bw.fit.common.model;

import java.awt.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Test<T> {


	public static void main(String[] args) {
		JSONObject jsonObject =new JSONObject();
		LogUser user =new LogUser();
		user.setCompany_id("123");
		user.setUser_cd("admin");
		LogUser logUser = (JSONObject.parseObject("{'company_id':'123','user_cd':'admin'}",LogUser.class));
		System.out.println(logUser.getCompany_id());
	}
}
