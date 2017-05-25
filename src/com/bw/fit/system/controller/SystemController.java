package com.bw.fit.system.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;

import com.bw.fit.common.model.CommonModel;
import com.bw.fit.common.model.LogUser;
import com.bw.fit.common.util.PropertiesUtil;
import com.bw.fit.common.util.PubFun;
import com.bw.fit.system.service.SystemService;

@Controller
public class SystemController {
	@Autowired
	private SystemService systemService ;
	/***
	 * 系统登录
	 * 
	 * @return 成功后登录主页，失败就返回登录页
	 * @exception
	 * @author yangh
	 */
	@RequestMapping("system/login")
	public String normalLogin(@Valid @ModelAttribute LogUser user,
			CommonModel c, BindingResult result, HttpServletRequest request,
			HttpSession session,Model model) {
		try {
			if(result.hasErrors()){
				FieldError error = result.getFieldError(); 
				model.addAttribute("errorMsg", error.getDefaultMessage());
				return "common/loginPage"; 
			}		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/***
		 * 是否可以异地登录
		 */
		if("false".equalsIgnoreCase(PropertiesUtil.getValueByKey("user.multi.login"))){
			JSONObject j = systemService.getOnLineSituation(session,user,request.getServletContext()) ;
			if("1".equals(j.get("res"))){
				model.addAttribute("errorMsg", j.get("msg"));
				return "common/loginPage"; 
			}
		}
		user.setIp(PubFun.getIpAddr(request));
		session.setAttribute("LogUser", user);
		return "common/onlinePage";
	}

	/***
	 * 系统登出
	 * 
	 * @return 返回登录页
	 * @exception
	 * @author yangh
	 */
	@RequestMapping("system/logout")
	public String logout(@ModelAttribute LogUser user,
			SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		return "common/loginPage";
	}
}
