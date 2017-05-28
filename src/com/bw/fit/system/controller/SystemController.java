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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bw.fit.common.model.CommonModel;
import com.bw.fit.common.model.LogUser;
import com.bw.fit.common.util.PropertiesUtil;
import com.bw.fit.common.util.PubFun;
import com.bw.fit.system.model.Staff;
import com.bw.fit.system.service.SystemService;

@RequestMapping("system")
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
	@RequestMapping("/login")
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
		/**
		 * 密码校验
		 */
		JSONObject j2 = systemService.getPwdCheckResult(user);
		if("1".equals(j2.get("res"))){
			model.addAttribute("errorMsg", j2.get("msg"));
			return "common/loginPage"; 
		}
		/***
		 * 是否可以俩处登录
		 */
		if("false".equalsIgnoreCase(PropertiesUtil.getValueByKey("user.multi.login"))){
			JSONObject j = systemService.getOnLineSituation(session,user,request.getServletContext()) ;
			if("1".equals(j.get("res"))){
				model.addAttribute("errorMsg", j.get("msg"));
				return "common/loginPage"; 
			}
		}
		c.setStaff_number(user.getUser_cd());
		Staff staff = systemService.getStaffInfoByNumber(c);
		user.setUser_name(staff.getStaff_name());
		user.setUser_id(staff.getFdid());
		user.setUser_cd(staff.getStaff_number());
		user.setCompany_id(staff.getCompany_id());
		user.setCompany_name(staff.getCompany_name());
		user.setIp(PubFun.getIpAddr(request));
		c.setFdid(staff.getFdid());
		user.setRoles(systemService.getRoleListByStaffId(c));
		user.setPostions(systemService.getPostionListByStaffId(c));
		//user.setMac(PubFun.getMACAddress(user.getIp()));
		user.setMenuAuthTreeJson(systemService.getMenuTreeJsonByStaffId(c).toJSONString());
		String  menuTreeJson = systemService.getMenuTreeJsonByStaffId(c).toJSONString() ;
		model.addAttribute("menuTreeJson", menuTreeJson);
		session.setAttribute("LogUser", user);
		return "common/homePage";
	}

	/***
	 * 系统登出
	 * 
	 * @return 返回登录页
	 * @exception
	 * @author yangh
	 */
	@RequestMapping("/logout")
	public String logout(@ModelAttribute LogUser user,
			SessionStatus sessionStatus) {
		sessionStatus.setComplete();
		return "common/loginPage";
	}
	/***
	 * 去往登录页Nav
	 */
	@RequestMapping("/gotoLoginNav")
	public String gotoLoginNav(@ModelAttribute LogUser user,
			SessionStatus sessionStatus) { 
		return "common/loginPage";
	}
	
	/***
	 * 从菜单跳到NavTab页
	 * 
	 */
	@RequestMapping("/gotoIFramePage/{path}/{url}/{defaultAction}")
	public String gotoIFramePage(@PathVariable("path") String path,
			@PathVariable("url") String url,
			@PathVariable("defaultAction") String defaultAction,Model model,
			final RedirectAttributes redirectAttributes){
		if(null!=defaultAction || !"".equals(defaultAction)|| !"-9".equals(defaultAction)){
			return "redirect:" + defaultAction ;	
		}
		return path+"/"+url;
	}
}
