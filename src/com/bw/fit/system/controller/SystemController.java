package com.bw.fit.system.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.bw.fit.common.util.PubFun.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bw.fit.common.dao.CommonDao;
import com.bw.fit.common.model.CommonModel;
import com.bw.fit.common.model.LogUser;
import com.bw.fit.common.service.CommonService;
import com.bw.fit.common.util.AjaxBackResult;
import com.bw.fit.common.util.PropertiesUtil;
import com.bw.fit.common.util.PubFun;
import com.bw.fit.system.lambda.SystemLambda;
import com.bw.fit.system.model.Attachment;
import com.bw.fit.system.model.Company;
import com.bw.fit.system.model.Staff;
import com.bw.fit.system.persistence.BaseConditionVO;
import com.bw.fit.system.service.SystemService;

@RequestMapping("system")
@Controller
public class SystemController {
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
			CommonModel c, BindingResult result, HttpServletRequest request,
			HttpSession session, Model model) {
		try {
			if (result.hasErrors()) {
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
		if (j2 != null && "1".equals(j2.get("res"))) {
			model.addAttribute("errorMsg", j2.get("msg"));
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
		String menuTreeJson = systemService.getMenuTreeJsonByStaffId(c)
				.toJSONString();
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
			Model model) {
		CommonModel c = new CommonModel();
		c.setDict_value("ORGTYPE");
		model.addAttribute("OrgTypeList", systemService.getDictInfo(c));
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
	 */
	@RequestMapping(value = "createCompany", method = RequestMethod.POST)
	public ModelAndView createCompany(@Valid @ModelAttribute Company company,
			BindingResult result, HttpSession session) {
		JSONObject json = new JSONObject();
		CommonModel c = new CommonModel();
		AjaxBackResult a = new AjaxBackResult();
		try {
			if (result.hasErrors()) {
				FieldError error = result.getFieldError();
				json.put("res", "1");
				json.put("msg", error.getDefaultMessage());
				return a.returnAjaxBack(json);
			}
			systemService.fillCommonField(c, session);
			c.setCompany_address(company.getCompany_address());
			c.setCompany_name(company.getCompany_name());
			c.setCompany_order(company.getCompany_order());
			c.setCompany_type_id(company.getCompany_type_id());
			c.setParent_company_id(company.getParent_company_id());
			systemService.createCompany(c);
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
		CommonModel c = new CommonModel();
		AjaxBackResult a = new AjaxBackResult();
		try {
			if (result.hasErrors()) {
				FieldError error = result.getFieldError();
				json.put("res", "1");
				json.put("msg", error.getDefaultMessage());
				return a.returnAjaxBack(json);
			}
			systemService.fillCommonField(c, session);
			c.setStaff_name(staff.getStaff_name());
			c.setStaff_number(staff.getStaff_number());
			systemService.createCompany(c);
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
	@RequestMapping("openAttachmentPage/{foreign_id}")
	public String openAttachmentPage(Model model,
			@PathVariable String foreign_id, @ModelAttribute CommonModel c,
			HttpSession session) {
		model.addAttribute("foreign_id", foreign_id);
		model.addAttribute("param", c);
		c.setSql("systemSql.getAttachmentList");
		List list = commonDao.getListData(c.getSql(), c);
		model.addAttribute("attList", list);
		return "system/attachmentPage";
	}

	/****
	 * 多个附件一次性保存
	 * 
	 * @param servletRequest
	 * @param uploadFile
	 * @param files
	 * @param session
	 * @param fid
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/attachment_upload_multi/{fid}", method = RequestMethod.POST)
	public String saveUploadFileMulti(HttpServletRequest servletRequest,
			@ModelAttribute Attachment attachment, HttpSession session,
			@PathVariable String fid, ModelAndView model,
			HttpServletRequest req, HttpServletResponse response) {
		model.addObject("foreign_id", fid);

		String savePath = req.getSession().getServletContext().getRealPath("");
		savePath = savePath + "d:\\";
		// 把文件上传到服务器指定位置，并向前台返回文件名
		DiskFileItemFactory fac = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(fac);
		upload.setHeaderEncoding("utf-8");
		List fileList = null;
		try {
			// 文件类型解析req
			fileList = upload.parseRequest(req);
		} catch (FileUploadException ex) {
			// 终止文件上传，此处抛出异常
			ex.printStackTrace();
		}
		Iterator it = fileList.iterator();
		while (it.hasNext()) {
			String extName = "";
			FileItem item = (FileItem) it.next();
			if (!item.isFormField()) {
				String name = item.getName();
				String type = item.getContentType();
				if (item.getName() == null || item.getName().trim().equals("")) {
					continue;
				}
				File file = new File(savePath + name);
				try {
					// 将文件存入本地服务器
					item.write(file);
					// 向前台返回文件名
					PrintWriter pw = response.getWriter();
					pw.print(name);
					pw.close();
					pw.flush();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return "system/attachmentPage";
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
	@RequestMapping("openSysAddressBook/{params}/{objType}/{selectMulti}/{uuid}")
	public String getChildComps(@PathVariable("params") String params,
			@PathVariable("objType") String objType,
			@PathVariable("uuid") String uuid,
			@PathVariable("selectMulti") boolean selectMulti, Model model,
			BaseConditionVO vo, @ModelAttribute CommonModel c,
			HttpSession session) {
		JSONObject json = new JSONObject();
		c.setFdid(params);
		model.addAttribute("selectMulti", selectMulti);
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
		model.addAttribute("uuid",uuid);
		
		// 查询待选列表
		
		return "system/selectObjByTreePage";
	}
	/*****
	 * 根据关键词查询
	 * @param c
	 * @param model
	 * @return
	 */
	@RequestMapping("searchObjByKeyWds")
	public String searchObjByKeyWds(@ModelAttribute CommonModel c, Model model ) {
		model.addAttribute("orgTreeJSON", c.getTemp_str3());
		model.addAttribute("objTypeString", c.getDesp());
		model.addAttribute("comps_str", c.getDict_name());
		model.addAttribute("uuid",c.getUUID());
		
		c.setTemp_list(Arrays.asList(c.getTemp_str2().split(",")));
		List<CommonModel> list = systemService.getObjByKeyWds(c,c.getDesp());
		list = list.parallelStream().filter(x -> {
			return isContains(x.getKeyWords(), c.getKeyWords());
		}).collect(Collectors.toList());
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
				cc.setTemp_str4("checked");
			} else {
				cc.setTemp_str4("disabled");
			}
			listM.add(cc);
		}
		model.addAttribute("objType", listM);
		return "system/selectObjByTreePage";
	}
	/****
	 * 根据左侧组织树查询
	 * @param c
	 * @param model
	 * @return
	 */

	@RequestMapping("searchObjByComp/{compId}")
	public String searchObjByComp(@ModelAttribute CommonModel c,@PathVariable("compId") String compId, Model model) {
		model.addAttribute("orgTreeJSON", c.getTemp_str3());
		model.addAttribute("comps_str", c.getDict_name());
		model.addAttribute("uuid",c.getUUID()); 
		
		c.setTemp_list(Arrays.asList(compId.split(",")));
		List<CommonModel> list = systemService.getObjByKeyWds(c,c.getDesp());
		list = list.parallelStream().filter(x -> {
			return isContains(x.getKeyWords(), c.getKeyWords());
		}).collect(Collectors.toList());
		model.addAttribute("waitList", list);
		return "system/selectObjByTreePage";
	}
}
