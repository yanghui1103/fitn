package com.bw.fit.system.controller;

import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.bw.fit.common.util.PubFun.*;

import javax.imageio.ImageIO;
import javax.security.auth.Subject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.session.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import Decoder.BASE64Decoder;

import com.bw.fit.common.dao.CommonDao;
import com.bw.fit.common.model.CommonModel;
import com.bw.fit.common.model.LogUser;
import com.bw.fit.common.model.RbackException;
import com.bw.fit.common.service.CommonService;
import com.bw.fit.common.util.AjaxBackResult;
import com.bw.fit.common.util.PropertiesUtil;
import com.bw.fit.common.util.PubFun;
import com.bw.fit.system.lambda.SystemLambda;
import com.bw.fit.system.model.Attachment;
import com.bw.fit.system.model.Company;
import com.bw.fit.system.model.FFile;
import com.bw.fit.system.model.Role;
import com.bw.fit.system.model.Staff;
import com.bw.fit.system.model.To_read;
import com.bw.fit.system.persistence.BaseConditionVO;
import com.bw.fit.system.service.SystemService;

@RequestMapping("system")
@Controller
public class SystemController {
	private Log log = LogFactory.getLog(this.getClass());	
	@Autowired
	private SystemService systemService;
	@Autowired
	private CommonDao commonDao;

	/***
	 * 系统登录
	 * 
	 * @return 成功后登录主页，失败就返回登录页
	 * @exception
	 * @author yangh
	 */
	@RequestMapping("/login")
	public String normalLogin(@Valid @ModelAttribute LogUser user,
			BindingResult result, HttpServletRequest request, HttpSession session, Model model) {
		try {
			if (result.hasErrors()) {
				FieldError error = result.getFieldError();
				model.addAttribute("errorMsg", error.getDefaultMessage());
				return "common/loginPage";
			}
			// 获取存放在session中的验证码
			// String code = (String)
			// request.getSession().getAttribute("verificationCode");
			// 获取页面提交的验证码
			// String inputCode = user.getVerificationCode();
			// if(!code.toLowerCase().equals(inputCode.toLowerCase())) { //
			// 验证码不区分大小写
			// model.addAttribute("errorMsg", "验证码错误");
			// return "common/loginPage";
			// }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			model.addAttribute("errorMsg", "登录失败");
			return "common/loginPage";
		} 
		/***
		 * 是否可以俩处登录
		 */
		if ("false".equalsIgnoreCase(PropertiesUtil
				.getValueByKey("user.multi.login"))) {
			JSONObject j = systemService.getOnLineSituation(session, user,
					request.getServletContext());
			if ("1".equals(j.get("res"))) {
				model.addAttribute("errorMsg", j.get("msg"));
				return "common/loginPage";
			}
		}
		/****开始shiro登录*****/
		try {
			UsernamePasswordToken token = new UsernamePasswordToken(user.getUser_cd(),user.getPasswd());
			org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
			
			currentUser.login(token);
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
			model.addAttribute("errorMsg", "登录失败,权限拦截:" + e.getMessage());
			return "common/loginPage";
		}
		CommonModel c = new CommonModel();
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
		// user.setMac(PubFun.getMACAddress(user.getIp()));
		user.setMenuAuthTreeJson(systemService.getMenuTreeJsonByStaffId(c)
				.toJSONString());
		session.setAttribute("LogUser", user);
		return "common/indexPage";
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
	public String companyList(Model model) {
		model.addAttribute("listModel", "cccccc");
		return "system/companyListPage";
	}

	/***
	 * 从菜单跳到NavTab页
	 * 
	 * @throws Exception
	 * @throws ClientProtocolException
	 * 
	 */
	@RequestMapping("gotoIFramePage/{path}/{url}")
	public ModelAndView gotoIFramePage(@PathVariable("path") String path,
			@PathVariable("url") String url, RedirectAttributes attr,
			Model model, HttpSession session) {
		CommonModel c = new CommonModel();
		c.setDict_value("ORGTYPE");
		model.addAttribute("OrgTypeList", systemService.getDictInfo(c));
		Integer ing = new java.util.Random().nextInt(999999) + 1;
		model.addAttribute("digitId", ing);
		model.addAttribute("uuid", getUUID());

		return new ModelAndView(path + "/" + url);
	}

	/***
	 * 组织列表
	 */
	@RequestMapping("companyList/{params}")
	public String companyList(@PathVariable("params") String params,
			Model model, BaseConditionVO vo, @ModelAttribute CommonModel c,
			HttpSession session) {
		model.addAttribute("param", c);
		if (params.contains(PropertiesUtil.getValueByKey("system.delimiter"))) {
			String[] array = params.split(PropertiesUtil
					.getValueByKey("system.delimiter"));
			c.setTemp_list(Arrays.asList(array));
		} else {
			String fdid = ((LogUser) session.getAttribute("LogUser"))
					.getCompany_id();
			List<CommonModel> list = systemService
					.getChildCompByCurrentComp(fdid);
			c.setTemp_list(list.stream().map(CommonModel::getFdid)
					.collect(Collectors.toList()));
		}

		c.setSql("systemSql.getCompanyList");
		List<CommonModel> list = systemService.getCommonList(c);
		list = list
				.parallelStream()
				.filter(x -> {
					return isContains(x.getCompany_name(), c.getKeyWords())
							|| isContains(x.getCompany_address(),
									c.getKeyWords())
							|| isContains(x.getCompany_type_name(),
									c.getKeyWords());
				}).collect(Collectors.toList());
		List<CommonModel> list2 = list.stream()
				.skip((vo.getPageNum() - 1) * vo.getPageSize())
				.limit(vo.getPageSize()).collect(Collectors.toList());
		vo.setTotalCount((int) list.stream().count());
		model.addAttribute("companyList", list2);
		model.addAttribute("vo", vo);
		return "system/companyListPage";
	}

	/***
	 * 修改密码
	 */
	@RequestMapping("changePwd")
	public ModelAndView changePwd(@ModelAttribute CommonModel c) {
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
	public String dataDictPage(@PathVariable("params") String params,
			Model model, @ModelAttribute CommonModel c, HttpSession session) {
		JSONObject json = new JSONObject();
		c.setParent_id("0");
		List<CommonModel> list = systemService.getDataDictList(c);
		if (list.size() < 1) {
			json = new JSONObject();
			json.put("res", "1");
			json.put("msg", "无数据");
			model.addAttribute("dataDictTreeJson", json);
		} else {
			json = new JSONObject();
			json.put("res", "2");
			json.put("msg", "有数据");
			JSONArray array = new JSONArray();
			for (CommonModel cc : list) {
				JSONObject json1 = new JSONObject();
				json1.put("id", cc.getFdid());
				json1.put("pId", cc.getParent_id());
				json1.put("name", cc.getDict_name());
				json1.put("t", "id=" + cc.getFdid());
				json1.put("open", true);
				array.add(json1);
			}
			json.put("list", array);
			model.addAttribute("dataDictTreeJson", json);
		}
		return "system/dataDictPage";
	}

	/***
	 * 点击节点，查询出 其子节点列表信息
	 */
	@RequestMapping("dictlist/{id}")
	public String getdictlist(Model model, @PathVariable("id") String id) {
		model.addAttribute("id", id);
		CommonModel c = new CommonModel();
		c.setParent_id(id);
		List<CommonModel> list = systemService.getDataDictList(c);
		model.addAttribute("itemList", list);
		return "system/dictItemPage";
	}

	/**
	 * 当前用户， 指定菜单ID 将受控制的按钮和不受控制的按钮获取到 yangh
	 */
	@RequestMapping("getOperationsByMenuId/{BtnPrefixCode}")
	@ResponseBody
	public JSONObject getOperationsByMenuId(
			@PathVariable(value = "BtnPrefixCode") String BtnPrefixCode,
			HttpServletRequest requset, HttpSession session) {
		JSONObject json = new JSONObject();
		AjaxBackResult a = new AjaxBackResult();
		CommonModel c = new CommonModel();
		c.setStaff_id(((LogUser) session.getAttribute("LogUser")).getUser_id());
		c.setTemp_str1(BtnPrefixCode);
		json = systemService.getOperationsByMenuId(c);
		return json;
	}

	/**
	 * 新建组织
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "createCompany", method = RequestMethod.POST)
	public ModelAndView createCompany(@Valid @ModelAttribute Company company,
			BindingResult result, HttpSession session) {
		JSONObject json = new JSONObject();
		CommonModel c = new CommonModel();
		AjaxBackResult a = new AjaxBackResult();

		if (result.hasErrors()) {
			FieldError error = result.getFieldError();
			json.put("res", "1");
			json.put("msg", error.getDefaultMessage());
			return a.returnAjaxBack(json);
		}
		systemService.fillCommonField(c, session, false);
		c.setFdid(company.getFdid());
		c.setCompany_address(company.getCompany_address());
		c.setCompany_name(company.getCompany_name());
		c.setCompany_order(company.getCompany_order());
		c.setCompany_type_id(company.getCompany_type_id());
		c.setParent_company_id(company.getParent_company_id().replace(";", ""));

		try {
			c.setSql("systemSql.createCompany");
			systemService.insert(c);
			json.put("res", "2");
			json.put("msg", "执行成功");
		} catch (RbackException e) {
			// TODO Auto-generated catch block
			json = new JSONObject();
			json.put("res", e.getRes());
			json.put("msg", e.getMsg());
			e.printStackTrace();
		}
		return a.returnAjaxBack(json);
	}

	/****
	 * @author yangh 根据组织ids查询出所有有效的 用户
	 */
	@RequestMapping("userList/{params}")
	public String userList(Model model, @PathVariable String params,
			BaseConditionVO vo, @ModelAttribute CommonModel c,
			HttpSession session) {
		model.addAttribute("param", c);
		if (params.contains(PropertiesUtil.getValueByKey("system.delimiter"))) {
			String[] array = params.split(PropertiesUtil
					.getValueByKey("system.delimiter"));
			c.setTemp_list(Arrays.asList(array));
		} else {
			String fdid = ((LogUser) session.getAttribute("LogUser"))
					.getCompany_id();
			List<CommonModel> list = systemService
					.getChildCompByCurrentComp(fdid);
			c.setTemp_list(list.stream().map(CommonModel::getFdid)
					.collect(Collectors.toList()));
		}

		List<CommonModel> list = systemService.getuserList(c);
		List<CommonModel> list2 = list.stream()
				.skip((vo.getPageNum() - 1) * vo.getPageSize())
				.limit(vo.getPageSize()).collect(Collectors.toList());
		vo.setTotalCount((int) list.stream().count());
		model.addAttribute("userList", list2);
		model.addAttribute("vo", vo);
		return "system/userListPage";
	}

	/****
	 * @author yangh 新建用户
	 */
	@RequestMapping(value = "createStaff", method = RequestMethod.POST)
	public ModelAndView createStaff(@Valid @ModelAttribute Staff staff,
			BindingResult result, HttpSession session) {
		JSONObject json = new JSONObject();
		AjaxBackResult a = new AjaxBackResult();
		try {
			if (result.hasErrors()) {
				FieldError error = result.getFieldError();
				json.put("res", "1");
				json.put("msg", error.getDefaultMessage());
				return a.returnAjaxBack(json);
			}

			if (commonDao.getListData("systemSql.getStaffNotDelStfId", staff)
					.size() > 0) {
				json.put("res", "1");
				json.put("msg", "帐号已经被占用");
				return a.returnAjaxBack(json);
			}
			systemService.fillCommonField(staff, session, false);
			staff.setCompany_id(staff.getCompany_id().replace(
					PropertiesUtil.getValueByKey("system.delimiter"), ""));
			// staff.setStaff_group_id(staff.getStaff_group_id().replace(PropertiesUtil.getValueByKey("system.delimiter"),
			// ""));
			// staff.setRole_id(staff.getRole_id().replace(PropertiesUtil.getValueByKey("system.delimiter"),
			// ""));
			// staff.setPostion_id(staff.getPostion_id().replace(PropertiesUtil.getValueByKey("system.delimiter"),
			// ""));
			systemService.createStaff(staff);
			json.put("res", "2");
			json.put("msg", "执行成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			json.put("res", "1");
			json.put("msg", e.getLocalizedMessage());
			e.printStackTrace();
		}
		return a.returnAjaxBack(json);
	}

	/***
	 * @author yangh 用户组列表
	 */
	@RequestMapping("staffGrpList/{params}")
	public String staffGrpList(Model model, BaseConditionVO vo,
			@PathVariable String params, @ModelAttribute CommonModel c,
			HttpSession session) {
		model.addAttribute("param", c);
		List<CommonModel> list = systemService.getstaffGrpList(c);
		List<CommonModel> list2 = list.stream()
				.skip((vo.getPageNum() - 1) * vo.getPageSize())
				.limit(vo.getPageSize()).collect(Collectors.toList());
		vo.setTotalCount((int) list.stream().count());
		model.addAttribute("staffGrpList", list2);
		model.addAttribute("vo", vo);
		return "system/staffGrpListPage";
	}

	/****
	 * 角色列表
	 */
	@RequestMapping("roleList/{params}")
	public String roleList(Model model, BaseConditionVO vo,
			@PathVariable String params, @ModelAttribute CommonModel c,
			HttpSession session) {
		model.addAttribute("param", c);
		c.setSql("systemSql.getroleList");
		List<CommonModel> list = systemService.getCommonList(c);
		list = list.parallelStream().filter(x -> {
			return isContains(x.getRole_name(), c.getKeyWords());
		}).collect(Collectors.toList());
		List<CommonModel> list2 = list.stream()
				.skip((vo.getPageNum() - 1) * vo.getPageSize())
				.limit(vo.getPageSize()).collect(Collectors.toList());
		vo.setTotalCount((int) list.stream().count());
		model.addAttribute("roleList", list2);
		model.addAttribute("vo", vo);
		return "system/roleListPage";
	}

	/****
	 * 岗位列表
	 */
	@RequestMapping("postionList/{params}")
	public String postionList(Model model, BaseConditionVO vo,
			@PathVariable String params, @ModelAttribute CommonModel c,
			HttpSession session) {
		model.addAttribute("param", c);
		c.setSql("systemSql.getpostionList");
		List<CommonModel> list = systemService.getCommonList(c);
		list = list.parallelStream().filter(x -> {
			return isContains(x.getPostion_name(), c.getKeyWords());
		}).collect(Collectors.toList());
		List<CommonModel> list2 = list.stream()
				.skip((vo.getPageNum() - 1) * vo.getPageSize())
				.limit(vo.getPageSize()).collect(Collectors.toList());
		vo.setTotalCount((int) list.stream().count());
		model.addAttribute("postionList", list2);
		model.addAttribute("vo", vo);
		return "system/postionListPage";
	}

	/*****
	 * 定时任务列表
	 * 
	 * @param model
	 * @param vo
	 * @param params
	 * @param c
	 * @param session
	 * @return
	 */
	@RequestMapping("jobList/{params}")
	public String jobList(Model model, BaseConditionVO vo,
			@PathVariable String params, @ModelAttribute CommonModel c,
			HttpSession session) {
		model.addAttribute("param", c);
		c.setSql("systemSql.getjobList");
		List<CommonModel> list = systemService.getCommonList(c);
		list = list.parallelStream().filter(x -> {
			return isContains(x.getJob_name(), c.getKeyWords());
		}).collect(Collectors.toList());
		List<CommonModel> list2 = list.stream()
				.skip((vo.getPageNum() - 1) * vo.getPageSize())
				.limit(vo.getPageSize()).collect(Collectors.toList());
		vo.setTotalCount((int) list.stream().count());
		model.addAttribute("jobList", list2);
		model.addAttribute("vo", vo);

		return "system/jobListPage";
	}

	/****
	 * 
	 */
	@RequestMapping("openAttachmentPage/{foreign_id}/{multi}")
	public String openAttachmentPage(Model model,
			@PathVariable(value = "foreign_id") String foreign_id,
			@PathVariable(value = "multi") boolean multi, HttpSession session) {
		model.addAttribute("foreign_id", foreign_id);
		model.addAttribute("multi", multi);
		CommonModel c = new CommonModel();
		c.setForeign_id(foreign_id);
		c.setSql("systemSql.getAttachmentList");
		List list = commonDao.getListData(c.getSql(), c);
		model.addAttribute("attList", list);
		return "system/attPage";
	}

	/****
	 * 多个附件一次性保存
	 * 
	 * @throws Exception
	 * 
	 */

	@RequestMapping(value = "/attachment_upload_multi/{fid}", method = RequestMethod.POST)
	public ModelAndView saveUploadFileMulti(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String fid,
			ModelAndView model, HttpSession session) throws Exception {
		JSONObject json = new JSONObject();
		AjaxBackResult a = new AjaxBackResult();
		Attachment aa = new Attachment();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		String pp = PropertiesUtil.getValueByKey("attachment.realPath");
		String savePath = request.getSession().getServletContext()
				.getRealPath("/")
				+ pp + "/";
		String fileName = "";
		for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			MultipartFile mf = entry.getValue();
			fileName = mf.getOriginalFilename();
			fileName = getUUID() + getFileTypeName(fileName); // 存入系统就是另外一个名词
			File uploadfile = new File(savePath + fileName);
			try {
				FileCopyUtils.copy(mf.getBytes(), uploadfile);
				aa.setFdid(getUUID());
				aa.setBefore_name(mf.getOriginalFilename());
				aa.setFile_name(fileName);
				aa.setForeign_id(fid);
				aa.setPath(pp);
				aa.setFile_size(mf.getSize());
				commonDao.insert("systemSql.saveUploadFile", aa);
			} catch (Exception ex) {
				// 终止文件上传，此处抛出异常
				ex.printStackTrace();
				json.put("res", "1");
				json.put("msg", "上传异常");
				return a.returnAjaxBack(json);
			}
		}
		json.put("res", "2");
		json.put("msg", "上传成功");
		return a.returnAjaxBack(json);
	}

	/***
	 * 
	 * @param params
	 *            如果0 就会把全部组织取出来 ，如果是当前组织id就会把当前组合和自组织取出
	 * @param model
	 * @param vo
	 * @param c
	 * @param session
	 * @return
	 */
	@RequestMapping("openSysAddressBook/{params}/{objType}/{selectMulti}/{uuid}/{elementId}")
	public String getChildComps(@PathVariable("params") String params,
			@PathVariable("objType") String objType,
			@PathVariable("uuid") String uuid,
			@PathVariable("elementId") String elementId,
			@PathVariable("selectMulti") boolean selectMulti, Model model,
			BaseConditionVO vo, @ModelAttribute CommonModel c,
			HttpSession session) {
		JSONObject json = new JSONObject();
		c.setFdid(params);
		model.addAttribute("selectMulti", selectMulti);
		model.addAttribute("elementId", elementId);
		c.setSql("systemSql.getChildCompByCurrentComp");
		List<CommonModel> list1 = systemService.getCommonList(c);

		list1 = list1
				.stream()
				.filter(t -> !t.getCompany_name().contains(
						PropertiesUtil.getValueByKey("system.top_company")))
				.distinct().collect(Collectors.toList());

		if (list1.size() < 1) {
			json.put("res", "1");
			json.put("msg", "无数据");
		} else {
			json.put("res", "2");
			json.put("msg", "数据");
			JSONArray array = new JSONArray();
			for (CommonModel cc : list1) {
				JSONObject j = new JSONObject();
				j.put("id", cc.getFdid());
				j.put("pId", cc.getParent_id());
				j.put("name", cc.getCompany_name());
				j.put("t", cc.getCompany_name());
				j.put("click", true);
				j.put("open", true);
				array.add(j);
			}
			json.put("list", array);
		}
		model.addAttribute("orgTreeJSON", json.toJSONString());
		// 所要查询的类型
		String[] array = objType.split("");
		String[] array_names = { "用户", "用户组", "岗位", "角色", "组织" };
		List<CommonModel> listM = new ArrayList<>();
		for (int i = 0; i < array.length; i++) {
			CommonModel cc = new CommonModel();
			cc.setTemp_str2(array[i]);
			cc.setTemp_str3(array_names[i]);
			if ("1".equals(array[i])) {
				cc.setTemp_str4("checked");
			} else {
				cc.setTemp_str4("disabled");
			}
			listM.add(cc);
		}
		model.addAttribute("objType", listM);
		model.addAttribute("objTypeString", objType);
		// 获取本次查询到的机构IDs
		List<String> list_comps = list1.stream().map(CommonModel::getFdid)
				.collect(Collectors.toList());

		String str1 = StringUtils.join(list_comps.toArray(), ",");
		model.addAttribute("comps_str", str1);
		model.addAttribute("uuid", uuid);

		// 查询已选列表
		c.setForeign_id(uuid);
		c.setElementId(elementId);
		c.setSql("systemSql.getObjIdsByFgId");
		List<CommonModel> list2 = systemService.getCommonList(c);
		if (list2.size() < 1) { // 如果这个外键id并关联主体
			return "system/selectObjByTreePage";
		}
		String[] a = list2.get(0).getFdid()
				.split(PropertiesUtil.getValueByKey("system.delimiter"));
		List<String> lis = Arrays.asList(a);
		c.setTemp_list(lis);
		if (lis.size() > 0) {
			c.setSql("systemSql.getSelectedIds");
			List<CommonModel> selectedList = systemService.getCommonList(c);
			for (CommonModel cc : selectedList) {
				if (!"-9".equals(cc.getStaff_number())) {
					Staff m = (Staff) commonDao.getOneData(
							"systemSql.getStaffInfoById", cc);
					cc.setDesp(m.getCompany_name() + ",岗位:" + cc.getDesp());
				}
			}
			model.addAttribute("selectedList", selectedList);
		}
		return "system/selectObjByTreePage";
	}

	/*****
	 * 根据关键词查询
	 * 
	 * @param c
	 * @param model
	 * @return
	 */
	@RequestMapping("searchObjByKeyWds")
	public String searchObjByKeyWds(@ModelAttribute CommonModel c, Model model) {
		model.addAttribute("orgTreeJSON", c.getTemp_str3());
		model.addAttribute("objTypeString", c.getDesp());
		model.addAttribute("comps_str", c.getDict_name());
		model.addAttribute("elementId", c.getElementId());
		model.addAttribute("uuid", c.getUUID());
		model.addAttribute("selectMulti", c.getMenu_name());

		c.setTemp_list(Arrays.asList(c.getTemp_str2().split(",")));
		List<CommonModel> list = systemService.getObjByKeyWds(c, c.getDesp());
		if (!"".equals(c.getKeyWords())) {
			list = list.parallelStream().filter(x -> {
				return isContains(x.getKeyWords(), c.getKeyWords());
			}).collect(Collectors.toList());
		}
		model.addAttribute("waitList", list);
		// 查询类型
		String[] array = c.getDesp().split("");
		String[] array_names = { "用户", "用户组", "岗位", "角色", "组织" };
		List<CommonModel> listM = new ArrayList<>();
		for (int i = 0; i < array.length; i++) {
			CommonModel cc = new CommonModel();
			cc.setTemp_str2(array[i]);
			cc.setTemp_str3(array_names[i]);
			if ("1".equals(array[i])) {
				cc.setTemp_str4("checked disabled");
			} else {
				cc.setTemp_str4("disabled");
			}
			listM.add(cc);
		}
		model.addAttribute("objType", listM);

		// 查询已选列表
		c.setForeign_id(c.getUUID());
		c.setElementId(c.getElementId());
		c.setSql("systemSql.getObjIdsByFgId");
		List<CommonModel> list2 = systemService.getCommonList(c);
		List<String> lis = list2.stream().map(CommonModel::getFdid)
				.collect(Collectors.toList());
		c.setTemp_list(lis);
		if (lis.size() > 0) {
			c.setSql("systemSql.getSelectedIds");
			List<CommonModel> selectedList = systemService.getCommonList(c);
			model.addAttribute("selectedList", selectedList);
		}
		return "system/selectObjByTreePage";
	}

	/****
	 * 根据左侧组织树查询
	 * 
	 * @param c
	 * @param model
	 * @return
	 */

	@RequestMapping("searchObjByComp/{compId}")
	public String searchObjByComp(@ModelAttribute CommonModel c,
			@PathVariable("compId") String compId, Model model) {
		model.addAttribute("orgTreeJSON", c.getTemp_str3());
		model.addAttribute("comps_str", c.getDict_name());
		model.addAttribute("uuid", c.getUUID());
		model.addAttribute("selectMulti", c.getMenu_name());

		c.setTemp_list(Arrays.asList(compId.split(",")));
		List<CommonModel> list = systemService.getObjByKeyWds(c, c.getDesp());
		if (!"".equals(c.getKeyWords())) {
			list = list.parallelStream().filter(x -> {
				return isContains(x.getKeyWords(), c.getKeyWords());
			}).collect(Collectors.toList());
		}
		model.addAttribute("waitList", list);
		return "system/selectObjByTreePage";
	}

	@RequestMapping("insertTempRelation/{foreign_id}/{objIds}/{elementId}")
	public ModelAndView insertTempRelation(
			@PathVariable("foreign_id") String foreign_id,
			@PathVariable("elementId") String elementId,
			@PathVariable("objIds") String objIds) {
		JSONObject json = new JSONObject();
		CommonModel c = new CommonModel();
		c.setForeign_id(foreign_id);
		c.setTemp_str1(objIds);
		c.setElementId(elementId);
		AjaxBackResult a = new AjaxBackResult();
		try {
			systemService.insertTempRelation(c);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a.returnAjaxBack(json);
	}

	/****
	 * 根据id查询组织详情
	 * 
	 * @param c
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("openEditCompany/{id}")
	public String openEditCompany(@ModelAttribute CommonModel c,
			@PathVariable("id") String id, Model model) {
		c.setFdid(id);
		c.setSql("systemSql.getCompanyDetails");
		Company cc = (Company) commonDao.getOneData(c.getSql(), c);

		model.addAttribute("model", cc);
		c.setDict_value("ORGTYPE");
		model.addAttribute("OrgTypeList", systemService.getDictInfo(c));
		return "system/editCompanyPage";
	}

	/***
	 * 保存修改组织
	 * 
	 * @param company
	 * @param result
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "updateCompany", method = RequestMethod.POST)
	public ModelAndView updateCompany(@Valid @ModelAttribute Company company,
			BindingResult result, HttpSession session) {
		JSONObject json = new JSONObject();
		CommonModel c = new CommonModel();
		AjaxBackResult a = new AjaxBackResult();
		if (result.hasErrors()) {
			FieldError error = result.getFieldError();
			json.put("res", "1");
			json.put("msg", error.getDefaultMessage());
			return a.returnAjaxBack(json);
		}
		systemService.fillCommonField(c, session, false);
		c.setFdid(company.getFdid());
		c.setCompany_address(company.getCompany_address());
		c.setCompany_name(company.getCompany_name());
		c.setCompany_order(company.getCompany_order());
		c.setCompany_type_id(company.getCompany_type_id());
		c.setParent_company_id(company.getParent_company_id().replace(";", ""));

		try {
			c.setSql("systemSql.updateCompany");
			systemService.update(c);
			json.put("res", "2");
			json.put("msg", "执行成功");
		} catch (RbackException e) {
			// TODO Auto-generated catch block
			json = new JSONObject();
			json.put("res", e.getRes());
			json.put("msg", e.getMsg());
			e.printStackTrace();
		}
		return a.returnAjaxBack(json);
	}

	@RequestMapping(value = "delCompany/{id}", method = RequestMethod.POST)
	public ModelAndView delCompany(@ModelAttribute CommonModel c,
			@PathVariable("id") String id, Model model) {
		JSONObject json = new JSONObject();
		AjaxBackResult a = new AjaxBackResult();
		c.setFdid(id);
		try {
			c.setSql("systemSql.delCompany");
			systemService.update(c);
			json.put("res", "2");
			json.put("msg", "执行成功");
		} catch (RbackException e) {
			// TODO Auto-generated catch block
			json = new JSONObject();
			json.put("res", e.getRes());
			json.put("msg", e.getMsg());
			e.printStackTrace();
		}
		return a.returnAjaxBack(json);
	}

	/****
	 * 删除用户
	 * 
	 * @param c
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "delStaff/{id}", method = RequestMethod.POST)
	public ModelAndView delStaff(@PathVariable("id") String id, Model model) {
		CommonModel c = new CommonModel();
		JSONObject json = new JSONObject();
		AjaxBackResult a = new AjaxBackResult();
		c.setFdid(id);
		try {
			c.setSql("systemSql.delStaff");
			systemService.update(c);
			json.put("res", "2");
			json.put("msg", "执行成功");
		} catch (RbackException e) {
			// TODO Auto-generated catch block
			json = new JSONObject();
			json.put("res", e.getRes());
			json.put("msg", e.getMsg());
			e.printStackTrace();
		}
		return a.returnAjaxBack(json);
	}

	/**
	 * 打开修改用户页
	 * 
	 * @param c
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("openUpdateStaff/{id}")
	public String openUpdateStaff(@ModelAttribute Staff c,
			@PathVariable("id") String id, Model model) {
		c.setFdid(id);
		c.setSql("systemSql.getStaffDetails");
		Staff cc = (Staff) commonDao.getOneData(c.getSql(), c);
		Map map = systemService.getRlPostGrpInfosByStaffId(c.getFdid());
		cc.setRole_id(((CommonModel) map.get("role")).getTemp_str1());
		cc.setRole_name(((CommonModel) map.get("role")).getTemp_str2());

		cc.setPostion_id(((CommonModel) map.get("postion")).getTemp_str1());
		cc.setPostion_name(((CommonModel) map.get("postion")).getTemp_str2());

		cc.setStaff_group_id(((CommonModel) map.get("staff_group"))
				.getTemp_str1());
		cc.setStaff_group_name(((CommonModel) map.get("staff_group"))
				.getTemp_str2());

		model.addAttribute("model", cc);
		return "system/updateStaffPage";
	}

	@RequestMapping(value = "updateStaff", method = RequestMethod.POST)
	public ModelAndView updateStaff(@Valid @ModelAttribute Staff staff,
			BindingResult result, HttpSession session) {
		JSONObject json = new JSONObject();
		AjaxBackResult a = new AjaxBackResult();
		if (result.hasErrors()) {
			FieldError error = result.getFieldError();
			json.put("res", "1");
			json.put("msg", error.getDefaultMessage());
			return a.returnAjaxBack(json);
		}
		try {
			systemService.updateStaff(staff);
			json.put("res", "2");
			json.put("msg", "执行成功");
		} catch (RbackException e) {
			// TODO Auto-generated catch block
			json = new JSONObject();
			json.put("res", e.getRes());
			json.put("msg", e.getMsg());
			e.printStackTrace();
		}
		return a.returnAjaxBack(json);
	}

	@RequestMapping(value = "createStaffGrp", method = RequestMethod.POST)
	public ModelAndView createStaffGrp(@Valid @ModelAttribute CommonModel c,
			HttpSession session) {
		JSONObject json = new JSONObject();
		AjaxBackResult a = new AjaxBackResult();
		try {
			systemService.fillCommonField(c, session, false);
			systemService.createStaffGrp(c);
			json.put("res", "2");
			json.put("msg", "执行成功");
		} catch (RbackException e) {
			// TODO Auto-generated catch block
			json = new JSONObject();
			json.put("res", e.getRes());
			json.put("msg", e.getMsg());
			e.printStackTrace();
		}
		return a.returnAjaxBack(json);
	}

	/***
	 * 删除用户组
	 * 
	 * @param id
	 * @param session
	 * @return
	 */
	@RequestMapping("deleteStaffGroup/{id}")
	public ModelAndView deleteStaffGroup(@PathVariable("id") String id,
			HttpSession session) {
		CommonModel c = new CommonModel();
		c.setFdid(id);
		JSONObject json = new JSONObject();
		AjaxBackResult a = new AjaxBackResult();
		try {
			systemService.fillCommonField(c, session, false);
			systemService.deleteStaffGroup(c);
			json.put("res", "2");
			json.put("msg", "执行成功");
		} catch (RbackException e) {
			// TODO Auto-generated catch block
			json = new JSONObject();
			json.put("res", e.getRes());
			json.put("msg", e.getMsg());
			e.printStackTrace();
		}
		return a.returnAjaxBack(json);
	}

	/***
	 * 打开修改用户组页面
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("openUpdateStaffGrpPage/{id}")
	public String openUpdateStaffGrpPage(@PathVariable("id") String id,
			Model model) {
		CommonModel c = new CommonModel();
		c.setFdid(id);
		CommonModel cc = systemService.getDetailsOfStaffGrp(c);
		model.addAttribute("model", cc);
		return "system/updateStaffGrpPage";
	}

	/***
	 * 保存修改用户组
	 * 
	 * @param c
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "updateStaffGrp", method = RequestMethod.POST)
	public ModelAndView updateStaffGrp(@Valid @ModelAttribute CommonModel c,
			HttpSession session) {
		JSONObject json = new JSONObject();
		AjaxBackResult a = new AjaxBackResult();
		try {
			systemService.fillCommonField(c, session, false);
			systemService.updateStaffGrp(c);
			json.put("res", "2");
			json.put("msg", "执行成功");
		} catch (RbackException e) {
			// TODO Auto-generated catch block
			json = new JSONObject();
			json.put("res", e.getRes());
			json.put("msg", e.getMsg());
			e.printStackTrace();
		}
		return a.returnAjaxBack(json);
	}

	/****
	 * 新建保存岗位
	 * 
	 * @param c
	 * @param session
	 * @return
	 */
	@RequestMapping("createPostion")
	public ModelAndView createPostion(@ModelAttribute CommonModel c,
			HttpSession session) {
		JSONObject json = new JSONObject();
		AjaxBackResult a = new AjaxBackResult();
		try {
			systemService.fillCommonField(c, session, false);
			json = systemService.createPostion(c);
		} catch (RbackException e) {
			// TODO Auto-generated catch block
			json = new JSONObject();
			json.put("res", e.getRes());
			json.put("msg", e.getMsg());
			e.printStackTrace();
		}
		return a.returnAjaxBack(json);

	}

	@RequestMapping("delPostion/{id}")
	public ModelAndView delPostion(@PathVariable("id") String id, Model model,
			HttpSession session) {
		CommonModel c = new CommonModel();
		c.setFdid(id);
		JSONObject json = new JSONObject();
		AjaxBackResult a = new AjaxBackResult();
		try {
			systemService.fillCommonField(c, session, false);
			systemService.delPostion(c);
			json.put("res", "2");
			json.put("msg", "执行成功");
		} catch (RbackException e) {
			// TODO Auto-generated catch block
			json = new JSONObject();
			json.put("res", e.getRes());
			json.put("msg", e.getMsg());
			e.printStackTrace();
		}
		return a.returnAjaxBack(json);
	}

	/***
	 * 打开修改岗位页
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("openUpdatePostionPage/{id}")
	public String openUpdatePostionPage(@PathVariable("id") String id,
			Model model) {
		CommonModel c = new CommonModel();
		c.setFdid(id);
		c.setSql("systemSql.getDetailsOfPostion");

		CommonModel cc = systemService.getOneCommnonData(c);
		model.addAttribute("model", cc);
		return "system/updatePostionPage";
	}

	@RequestMapping("updatePostion")
	public ModelAndView updatePostion(@ModelAttribute CommonModel c,
			HttpSession session) {
		JSONObject json = new JSONObject();
		AjaxBackResult a = new AjaxBackResult();
		try {
			systemService.fillCommonField(c, session, false);
			systemService.updatePostion(c);
			json.put("res", "2");
			json.put("msg", "执行成功");
		} catch (RbackException e) {
			// TODO Auto-generated catch block
			json = new JSONObject();
			json.put("res", e.getRes());
			json.put("msg", e.getMsg());
			e.printStackTrace();
		}
		return a.returnAjaxBack(json);
	}

	@RequestMapping("openCreateRolePage")
	public String openCreateRolePage(HttpSession session, Model model) {
		List<Role> list_role = ((LogUser) (session.getAttribute("LogUser")))
				.getRoles();
		List<String> list_str = list_role.stream().map(Role::getFdid)
				.collect(Collectors.toList());
		model.addAttribute("myRoles", list_role);
		CommonModel c = new CommonModel();
		c.setTemp_list(list_str);

		JSONObject js = systemService.getAuthTreeOfMyRole(c);
		StringBuffer sb = new StringBuffer();
		sb.append("{msg:\"存在权限\",res:\"2\",list:[{name:\"系统管理\",pId:\"0\",id:\"1\"},"
				+ "{name:\"系统\",pId:\"1\",id:\"10\"},{name:\"组织管理\",pId:\"10\",id:\"100\"},"
				+ "{name:\"用户管理\",pId:\"10\",id:\"101\"},{name:\"用户组管理\",pId:\"10\",id:\"102\"},"
				+ "{name:\"角色管理\",pId:\"10\",id:\"103\"},{name:\"岗位管理\",pId:\"10\",id:\"105\"},"
				+ "{name:\"新增\",pId:\"103\",id:\"10k2e44\"},{name:\"修改\",pId:\"103\",id:\"11k2e44\"},"
				+ "{name:\"删除\",pId:\"103\",id:\"12k2e44\"},{name:\"新增\",pId:\"105\",id:\"13k2e44\"},"
				+ "{name:\"修改\",pId:\"105\",id:\"14k2e44\"},{name:\"删除\",pId:\"105\",id:\"15k2e44\"},"
				+ "{name:\"修改\",pId:\"201\",id:\"16k2e44\"},{name:\"删除\",pId:\"201\",id:\"17k2e44\"},"
				+ "{name:\"新增\",pId:\"200\",id:\"18k2e44\"},{name:\"修改\",pId:\"200\",id:\"19k2e44\"},"
				+ "{name:\"新增\",pId:\"100\",id:\"1k2e44\"},{name:\"应用管理\",pId:\"0\",id:\"2\"},"
				+ "{name:\"应用管理\",pId:\"2\",id:\"20\"},{name:\"数据字典\",pId:\"20\",id:\"200\"},"
				+ "{name:\"定时任务管理\",pId:\"20\",id:\"201\"},{name:\"测试2\",pId:\"20\",id:\"202\"},"
				+ "{name:\"删除\",pId:\"200\",id:\"20k2e44\"},{name:\"新增\",pId:\"101\",id:\"2k2e44\"},"
				+ "{name:\"修改\",pId:\"100\",id:\"3k2e44\"},{name:\"删除\",pId:\"100\",id:\"4k2e44\"},"
				+ "{name:\"修改\",pId:\"101\",id:\"5k2e44\"},{name:\"删除\",pId:\"101\",id:\"6k2e44\"},"
				+ "{name:\"新增\",pId:\"102\",id:\"7k2e44\"},{name:\"修改\",pId:\"102\",id:\"8k2e44\"},"
				+ "{name:\"删除\",pId:\"102\",id:\"9k2e44\"}]}\")");
		model.addAttribute("treeJson", js.toJSONString());
		System.out.println(js.toJSONString());
		return "system/createRolePage";
	}

	@RequestMapping("createRole")
	public ModelAndView createRole(@Valid @ModelAttribute Role role,
			HttpSession session) {
		JSONObject json = new JSONObject();
		AjaxBackResult a = new AjaxBackResult();
		systemService.fillCommonField(role, session, true);
		json.put("res", "2");
		json.put("msg", "执行成功");

		try {
			systemService.createRole(role);
		} catch (RbackException e) {
			// TODO Auto-generated catch block
			json = new JSONObject();
			json.put("res", e.getRes());
			json.put("msg", e.getMsg());
			e.printStackTrace();
		}
		return a.returnAjaxBack(json);
	}

	/******
	 * 
	 * 选择一个角色，展示角色
	 * 
	 * @return
	 */
	@RequestMapping("system/openEditRole/{id}")
	public String openEditRole(@PathVariable String id, Model model) {
		CommonModel c = new CommonModel();
		c.setFdid(id);
		List<CommonModel> list = systemService.getroleList(c);
		model.addAttribute(
				"role_name",
				list.stream().filter(t -> id.equals(t.getFdid()))
						.collect(Collectors.toList()).get(0).getRole_name());

		CommonModel ccs = (CommonModel) commonDao.getOneData(
				"systemSql.getParentRole", c);
		if (ccs != null) {
			model.addAttribute("parent_role_name", ccs.getRole_name());
		} else {
			model.addAttribute("parent_role_name", "无");
		}
		// 根据角色id，查询出其菜单树
		CommonModel cs = (CommonModel) commonDao.getOneData(
				"systemSql.getParentRole", c);
		if (cs != null) {
			c.setFdid(cs.getFdid());
		}
		JSONObject json = systemService.getMenuTreeJson(c);
		model.addAttribute("menuTreeJson", json.toJSONString());
		model.addAttribute("role_id", id);
		return "system/editRolePage";
	}

	@RequestMapping("getEltCheckedOfRole/{roleId}/{menuId}")
	@ResponseBody
	public JSONObject getEltCheckedOfRole(
			@PathVariable(value = "roleId") String roleId,
			@PathVariable(value = "menuId") String menuId,
			HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		JSONObject json = new JSONObject();
		CommonModel c = new CommonModel();
		c.setTemp_str1(roleId);
		c.setTemp_str2(menuId);
		c.setFdid(roleId);
		json = systemService.getEltCheckedOfRole(c);
		return json;
	}

	/****
	 * 保存更新角色
	 * 
	 * @param c
	 * @param session
	 * @return
	 */
	@RequestMapping("updateRole")
	public ModelAndView updateRole(@ModelAttribute CommonModel c,
			HttpSession session) {
		JSONObject json = new JSONObject();
		json.put("res", "2");
		json.put("msg", "执行成功");
		AjaxBackResult a = new AjaxBackResult();
		try {
			systemService.fillCommonField(c, session, false);
			systemService.updateRole(c);
		} catch (RbackException e) {
			// TODO Auto-generated catch block
			json = new JSONObject();
			json.put("res", e.getRes());
			json.put("msg", e.getMsg());
			e.printStackTrace();
		}
		return a.returnAjaxBack(json);
	}

	@RequestMapping("alloctAuthority/{model}/{authoryId}/{objId}/{checked}")
	public ModelAndView removeAuthority2menu(
			@PathVariable(value = "checked") boolean checked,
			@PathVariable(value = "authoryId") String authoryId,
			@PathVariable(value = "model") String model,
			@PathVariable(value = "objId") String objId) {
		JSONObject json = new JSONObject();
		json.put("res", "2");
		json.put("msg", "执行成功");
		AjaxBackResult a = new AjaxBackResult();
		try {
			CommonModel c = new CommonModel();
			c.setFdid(authoryId);
			c.setTemp_str2(model);
			c.setTemp_str1(objId);
			c.setTemp_bool(checked);

			CommonModel ccs = (CommonModel) commonDao.getOneData(
					"systemSql.getParentRole", c);
			if (ccs == null) {
				json = new JSONObject();
				json.put("res", "1");
				json.put("msg", "系统最高角色权限不容许修改!");
				return a.returnAjaxBack(json);
			}

			systemService.removeAuthority2menu(c);
		} catch (RbackException e) {
			// TODO Auto-generated catch block
			json = new JSONObject();
			json.put("res", e.getRes());
			json.put("msg", e.getMsg());
			e.printStackTrace();
		}
		return a.returnAjaxBack(json);
	}

	/***
	 * 删除数据字典里的记录
	 * 
	 * @param item_id
	 * @return
	 */
	@RequestMapping("delDataDict/{item_id}")
	public ModelAndView delDataDict(
			@PathVariable(value = "item_id") String item_id) {
		JSONObject json = new JSONObject();
		json.put("res", "2");
		json.put("msg", "执行成功");
		AjaxBackResult a = new AjaxBackResult();
		try {
			CommonModel c = new CommonModel();
			c.setFdid(item_id);
			systemService.delDataDict(c);
		} catch (RbackException e) {
			// TODO Auto-generated catch block
			json = new JSONObject();
			json.put("res", e.getRes());
			json.put("msg", e.getMsg());
			e.printStackTrace();
		}
		return a.returnAjaxBack(json);
	}

	/***
	 * 数据字典，打开编辑页面
	 * 
	 * @return
	 */
	@RequestMapping("openEditDataDictPage/{item_id}")
	public String openEditDataDictPage(
			@PathVariable(value = "item_id") String item_id, Model model) {
		CommonModel c = new CommonModel();
		c.setFdid(item_id);
		c = (CommonModel) commonDao.getOneData("systemSql.getOneDictDetail", c);
		model.addAttribute("model", c);
		model.addAttribute("item_id", item_id);
		return "system/editDataDictPage";
	}

	@RequestMapping("openAddDataDictPage/{item_id}")
	public String openAddDataDictPage(
			@PathVariable(value = "item_id") String item_id, Model model) {
		CommonModel c = new CommonModel();
		c.setFdid(item_id);
		c = (CommonModel) commonDao.getOneData("systemSql.getOneDictDetail", c);
		model.addAttribute("model", c);
		model.addAttribute("item_id", item_id);
		return "system/addDataDictPage";
	}

	@RequestMapping("addDataDict")
	public ModelAndView addDataDict(@ModelAttribute CommonModel c) {
		JSONObject json = new JSONObject();
		json.put("res", "2");
		json.put("msg", "执行成功");
		AjaxBackResult a = new AjaxBackResult();
		try {

			systemService.addDataDict(c);
		} catch (RbackException e) {
			// TODO Auto-generated catch block
			json = new JSONObject();
			json.put("res", e.getRes());
			json.put("msg", e.getMsg());
			e.printStackTrace();
		}
		return a.returnAjaxBack(json);
	}

	@RequestMapping("updateDataDict")
	public ModelAndView updateDataDict(@ModelAttribute CommonModel c) {
		JSONObject json = new JSONObject();
		json.put("res", "2");
		json.put("msg", "执行成功");
		AjaxBackResult a = new AjaxBackResult();
		try {

			systemService.updateDataDict(c);
		} catch (RbackException e) {
			// TODO Auto-generated catch block
			json = new JSONObject();
			json.put("res", e.getRes());
			json.put("msg", e.getMsg());
			e.printStackTrace();
		}
		return a.returnAjaxBack(json);
	}

	@RequestMapping("attPage/{fg}")
	public String opentestAttachmentPage() {

		return "system/attPage";
	}

	@RequestMapping("cameraOfNT/{isReadOnly}/{foreignId}")
	public String opecameraOfNTPage(
			@PathVariable(value = "isReadOnly") String isReadOnly,
			@PathVariable(value = "foreignId") String foreignId, Model model) {
		model.addAttribute("isReadOnly", isReadOnly);
		model.addAttribute("foreignId", foreignId);
		return "common/cameraOfNTPage";
	}

	/**
	 * @throws FileUploadException
	 * @throws IOException
	 *             *
	 * 
	 */
	@RequestMapping(value = "doUpload", method = RequestMethod.POST)
	public String doUpload(@ModelAttribute Attachment attachment) {
		try {
			System.out.println("开始");
			System.out.println("length:" + attachment);
			// File file = file1.getFile();
			// String path =
			// request.getSession().getServletContext().getRealPath("upLoadFiles");
			// String fileName = file.getName();
			// // String fileName = new Date().getTime()+".jpg";
			// System.out.println(path);
			// File targetFile = new File(path, fileName);
			// if(!targetFile.exists()){
			// targetFile.mkdirs();
			// }
			//
			// //保存
			// try {
			// FileUtils.copyFile(file, targetFile);
			// // file.transferTo(targetFile);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "system/attPage";
	}

	@RequestMapping("createPhotoAttment")
	@ResponseBody
	public JSONObject createAttment(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		json.put("res", "2");
		json.put("msg", "上传成功");
		try {
			String format = PropertiesUtil
					.getValueByKey("system.attachment_photo_type");
			String after_name = getUUID() + "." + format;
			json.put("after_name", after_name);
			String bs_64 = request.getParameter("temp_str1");
			String foreign_id = request.getParameter("foreign_id");
			try {
				BufferedImage bufImg = ImageIO.read(new ByteArrayInputStream(
						new BASE64Decoder().decodeBuffer(bs_64)));

				ImageIO.write(
						bufImg,
						format,
						new File(PropertiesUtil
								.getValueByKey("system.attachment_path")
								+ after_name));

			} catch (IOException e) {
				json = new JSONObject();
				json.put("res", "1");
				json.put("msg", e.getMessage());
			}
			Attachment a = new Attachment();
			a.setBefore_name("image");
			a.setFile_name(after_name);
			a.setFile_size(1);
			a.setPath(PropertiesUtil.getValueByKey("system.attachment_path"));
			a.setForeign_id(foreign_id);
			a.setCreate_time(getSysDate());
			a.setFdid(getUUID());
			systemService.createAttment(a);
			json.put("file_name", after_name);
			json.put("fdid", a.getFdid());
		} catch (RbackException e) {
			// TODO Auto-generated catch block
			json = new JSONObject();
			json.put("res", e.getRes());
			json.put("msg", e.getMsg());
		}
		return json;
	}

	@RequestMapping("deleteAttahment/{fdid}")
	@ResponseBody
	public JSONObject deleteAttahment(@PathVariable(value = "fdid") String fdid) {
		Attachment a = new Attachment();
		JSONObject json = new JSONObject();
		json.put("res", "2");
		json.put("msg", "执行成功");
		a.setFdid(fdid);
		try {
			commonDao.delete("systemSql.deleteAttahment", a);
		} catch (RbackException e) {
			// TODO Auto-generated catch block
			json = new JSONObject();
			json.put("res", e.getRes());
			json.put("msg", e.getMsg());
		}
		return json;
	}

	/****
	 * 看图
	 * 
	 * @param response
	 * @param fdid
	 */
	@RequestMapping("lookPhotoAtt/{fileName}")
	public void lookPhotoAtt(HttpServletResponse response,
			@PathVariable(value = "fileName") String fileName) {
		try {
			File file = new File(
					PropertiesUtil.getValueByKey("system.attachment_path")
							+ "/" + fileName + ".JPG");
			response.setContentType("image/jpg");
			if (file.exists()) {
				byte[] buffer = new byte[1024];
				FileInputStream fis = null;
				BufferedInputStream bis = null;
				try {
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
					OutputStream os = response.getOutputStream();
					int i = bis.read(buffer);
					while (i != -1) {
						os.write(buffer, 0, i);
						i = bis.read(buffer);
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				} finally {
					if (bis != null) {
						bis.close();
					}
					if (fis != null) {
						fis.close();
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "getMessageInteractiveInfo/{userId}", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String getMessageInteractiveInfo(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			@PathVariable(value = "userId") String userId) throws Exception {
		JSONObject json = new JSONObject();
		To_read t = new To_read();
		t.setStaff_id(userId);
		CommonModel cc = (CommonModel) commonDao.getOneData(
				"systemSql.getMessageInteractiveInfo", t);
		List<To_read> cc_vip = commonDao.getListData(
				"systemSql.getMessageInteractiveVipInfo", t);
		session.invalidate(); // 让轮询的session失效
		if (cc != null && cc.getTemp_int1() > 0) {
			json.put("res", "2");
			if (cc_vip.size() > 0) {
				json.put(
						"vip_message",
						PropertiesUtil
								.getValueByKey("system.vip_message_title")
								+ cc_vip.get(0).getSubject());
				json.put("msg", "有新/未读信息");
				json.put(
						"url",
						PropertiesUtil.getValueByKey("system.default.url")
								+ PropertiesUtil
										.getValueByKey("system.wait_todoread_list"));
			} else {
				json.put("vip_message", "");
			}
			return json.toJSONString();
		}
		json.put("res", "1");
		json.put("msg", "无新/未读信息");
		return json.toJSONString();
	}

	@RequestMapping(value = "getWaitTodoList/{readOrDo}/{staff_id}/{itemId}/{keyWords}")
	public String getWaitTodoList(Model model,
			@PathVariable(value = "staff_id") String staff_id,
			@PathVariable(value = "keyWords") String keyWords,
			@PathVariable(value = "readOrDo") String readOrDo,
			@PathVariable(value = "itemId") String itemId,BaseConditionVO vo,HttpServletRequest request,HttpSession session) {
		if(!StringUtils.isEmpty(readOrDo) && readOrDo.equals("-9")){
			readOrDo = request.getParameter("readOrDo");
		}
		if(!StringUtils.isEmpty(staff_id) && staff_id.equals("my")){
			staff_id = ((LogUser)session.getAttribute("LogUser")).getUser_id();
		}
		if(!StringUtils.isEmpty(keyWords) && keyWords.equals("-9")){
			keyWords = request.getParameter("keyWords");
		}
		model.addAttribute("readOrDo", readOrDo);
		model.addAttribute("keyWords", keyWords);
		List<To_read> list = new ArrayList<>();		
		list = systemService.getWaitTodoList(staff_id,itemId,readOrDo,keyWords);

		list = list.stream()
				.skip((vo.getPageNum() - 1) * vo.getPageSize())
				.limit(vo.getPageSize()).collect(Collectors.toList());
		vo.setTotalCount((int) list.stream().count()); 
		model.addAttribute("vo", vo);
		model.addAttribute("to_list", list);
		return "system/toReadDoListPage";
	}

}
