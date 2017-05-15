package com.bw.fit.system.service.impl;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.bw.fit.common.service.impl.CommonServiceImpl;
import com.bw.fit.system.lambda.SystemLambda; 
import com.bw.fit.system.service.SystemService;

@Service
public class SystemServiceImpl implements SystemService {

	public String test() throws Exception{ 
		CommonServiceImpl c = new CommonServiceImpl();
		int g = (int) c.function(11, SystemLambda.f); // 
		return String.valueOf(g) ;
	}
}
