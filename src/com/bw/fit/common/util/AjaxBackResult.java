package com.bw.fit.common.util;

import org.json.simple.JSONObject;
import org.springframework.web.servlet.ModelAndView;

import com.bw.fit.common.controller.BaseController;

public class AjaxBackResult extends BaseController {

	public  ModelAndView returnAjaxBack(JSONObject json){ 
		String res = (String) json.get("res");
		String msg = (String) json.get("msg");
		if("2".equals(res)){
			return ajaxDoneSuccess(msg);
		}else{
			return ajaxDoneError(msg);
		}
	}
}
