package com.bw.fit.system.controller;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bw.fit.common.model.CommonModel;
import com.bw.fit.common.model.LogUser;
import com.bw.fit.common.util.AjaxBackResult;
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
		return "common/indexPage";
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
	@RequestMapping("/getCompanyList")
	public String companyList(Model model){
        model.addAttribute("listModel", "cccccc");
		return "system/companyListPage" ;
	}
	/***
	 * 从菜单跳到NavTab页
	 * @throws Exception 
	 * @throws ClientProtocolException 
	 * 
	 */
	@RequestMapping("gotoIFramePage/{path}/{url}")
	public ModelAndView gotoIFramePage(@PathVariable("path") String path,
			@PathVariable("url") String url,RedirectAttributes attr, Model model) {
//		if(null!=defaultAction && !"".equals(defaultAction) && !"-9".equals(defaultAction)){
//			String[] array = defaultAction.split("-");
//			CloseableHttpClient httpclient = HttpClients.createDefault(); 
//	        HttpGet httpget = new HttpGet(PropertiesUtil.getValueByKey("system.default.url") + array[0]+"/"+array[1]);   
//	        CloseableHttpResponse  response = httpclient.execute(httpget); 
//	        if(response.getStatusLine().getStatusCode()==200){ 
//		        // model.addAttribute("listModel", ((CommonModel)response.getEntity()));
//	        }
//			return new ModelAndView( path+"/"+url);
//		}
		return new ModelAndView( path+"/"+url);
	}
	/***
	 * 组织列表
	 */
	@RequestMapping("companyList/{params}")
	public String companyList(@PathVariable("params") String params,Model model,
			@ModelAttribute CommonModel c,
			HttpSession session){	
		model.addAttribute("param", c); 
		if(!"".equals(params) &&params !=null &&params.length()<1 ){
			String[] array = params.split("-");
			c.setTemp_list(Arrays.asList(array));
		}else{ 
			String fdid = ((LogUser)session.getAttribute("LogUser")).getCompany_id() ;
			List<CommonModel> list = systemService.getChildCompByCurrentComp(fdid);
			c.setTemp_list( list.parallelStream().map(CommonModel::getFdid).collect(Collectors.toList()));
		}
 		model.addAttribute("companyList", systemService.getCompanyList(c));
		return "system/companyListPage";
	}
	/***
	 * 修改密码
	 */
	@RequestMapping("changePwd")
	public ModelAndView changePwd(@ModelAttribute CommonModel c){
		JSONObject json = new JSONObject();
		AjaxBackResult a = new AjaxBackResult();
		try {
			json = systemService.updatePwd(c);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a.returnAjaxBack(json);
	}
	/***
	 * 打开数据字典页面
	 */
	@RequestMapping("dataDictPage/{params}")
	public String dataDictPage(@PathVariable("params") String params,Model model,
			@ModelAttribute CommonModel c,
			HttpSession session){
		
		return "system/dataDictPage";
	}
	
}
