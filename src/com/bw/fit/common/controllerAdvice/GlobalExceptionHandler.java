package com.bw.fit.common.controllerAdvice;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpStatus;
import org.json.simple.JSONObject;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;

import com.bw.fit.common.model.RbackException;


public class GlobalExceptionHandler {  
   
	@ExceptionHandler(Exception.class)  
    public String processUnauthenticatedException(NativeWebRequest request, Exception e,Model model) {  
        System.out.println("===========应用到所有@RequestMapping注解的方法，在其抛出Exception异常时执行"); 
        model.addAttribute("exceptionMessage", e.getLocalizedMessage());
        return "common/exceptionPage"; //逻辑视图名  
    }  
	@ExceptionHandler(value = RbackException.class)  
    @ResponseBody  
    public JSONObject missActionParam(HttpServletRequest req, RbackException e) throws Exception {  
        return makeErrorObj("接口有参数未传", req, e);  
    }  
	
	
	/** 
     * 构造错误信息 
     * 
     * @param msg 错误描述 
     * @param e   异常信息 
     * @return 
     */  
    private JSONObject makeErrorObj(String msg, HttpServletRequest req, Exception e) {  
        JSONObject obj = new JSONObject();  
        obj.put("res", "1");  
        obj.put("msg", msg + " (" + e.getMessage() + ")");   
        return obj;  
    }  
}  