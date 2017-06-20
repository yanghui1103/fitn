package com.bw.fit.log.service.impl;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

import static com.bw.fit.common.util.PubFun.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper; 
import com.bw.fit.common.dao.CommonDao;
import com.bw.fit.common.model.CommonModel;
import com.bw.fit.log.service.LogService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LogServiceImpl implements LogService, Ordered {
	@Autowired
	private CommonDao commonDao ;
	public void joinPointAccess(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("---joinPointAccess---");
        System.out.println("args[0]:" + pjp.getArgs()[0]);
        System.out.println("signature:" + pjp.getTarget().getClass());
        pjp.proceed();
        System.out.println("---joinPointAccess---");
    }
	/*****
	 * 收集日志
	 */
	@Override
	public void log(ProceedingJoinPoint pj) {
		try {
			CommonModel c = new CommonModel();
			c.setLogId(getUUID());
			ObjectMapper maper = new ObjectMapper();
			String jsonlist = maper.writeValueAsString(c);
			c.setLogContent(jsonlist);
			commonDao.insert("system.logOperation", c);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 1;
	}

	
}
