package com.bw.fit.common.controllerAdvice;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;


public class GlobalExceptionHandler {  
   
	@ExceptionHandler(Exception.class)  
    public String processUnauthenticatedException(NativeWebRequest request, Exception e,Model model) {  
        System.out.println("===========应用到所有@RequestMapping注解的方法，在其抛出Exception异常时执行"); 
        model.addAttribute("exceptionMessage", e.getLocalizedMessage());
        return "common/exceptionPage"; //返回一个逻辑视图名  
    }  
}  