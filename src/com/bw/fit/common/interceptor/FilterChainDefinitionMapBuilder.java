package com.bw.fit.common.interceptor;

import java.util.LinkedHashMap;

public class FilterChainDefinitionMapBuilder {

	public LinkedHashMap<String, String> buildFilterChainDefinitionmap(){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
			
		map.put("/system/login", "anon");
		map.put("/system/logout", "logout");

		map.put("/**", "authc");
		return map;
	}
}
