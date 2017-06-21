package com.bw.fit.common.util;

import java.util.Arrays;
import java.util.List;
import static com.bw.fit.common.util.PubFun.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;

import com.bw.fit.common.dao.CommonDao;
import com.bw.fit.common.model.CommonModel;
import com.fasterxml.jackson.databind.ObjectMapper; 
public class LogAspect implements  Ordered {
	@Autowired
	private CommonDao commonDao ;

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 1;
	}
	  public Object aroundMethod(ProceedingJoinPoint pjd) {
	        // String name = pjd.getSignature().getName();
	        List<Object> list = Arrays.asList(pjd.getArgs());
	        Object obj = null;
	        try {	  
	        	obj = pjd.proceed(); // 执行
	        	if("CommonModel".equalsIgnoreCase(((pjd.getArgs()[0]).getClass().getSimpleName()))){
		            
		            for(Object o:list){
			            CommonModel c = (CommonModel)o;
						c.setLogId(getUUID());
						ObjectMapper maper = new ObjectMapper();
						String jsonlist = maper.writeValueAsString(c);
						c.setLogContent(jsonlist);
						c.setReturnInfo(obj!=null?obj.toString():"-9"); // 返回json传入
						commonDao.insert("systemSql.logOperation", c);
		            }
	        	}
	        } catch (Throwable e) {
	            System.out.println("LogAspect:异常通知 ... , exception = " + e);
	            e.printStackTrace();
	        }
	        return obj;
	    } 
}
