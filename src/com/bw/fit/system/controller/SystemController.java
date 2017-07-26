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
import java.util.Map;
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
import com.bw.fit.common.model.RbackException;
import com.bw.fit.common.service.CommonService;
import com.bw.fit.common.util.AjaxBackResult;
import com.bw.fit.common.util.PropertiesUtil;
import com.bw.fit.common.util.PubFun;
import com.bw.fit.system.lambda.SystemLambda;
import com.bw.fit.system.model.Attachment;
import com.bw.fit.system.model.Company;
import com.bw.fit.system.model.Role;
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
	public ModelAndView createRole(@Valid @ModelAttribute Role role,HttpSession session ) {
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
	 * @return
	 */
	@RequestMapping("system/openEditRole/{id}") 
	public String openEditRole(@PathVariable String id,Model model){
		CommonModel c = new CommonModel();
		c.setFdid(id);
		List<CommonModel> list = systemService.getroleList(c);
		model.addAttribute("role_name",  list.stream().filter(t->id.equals(t.getFdid())).collect(Collectors.toList()).get(0).getRole_name());
		
		CommonModel ccs = (CommonModel)commonDao.getOneData("systemSql.getParentRole", c);
		if(ccs!=null){
			model.addAttribute("parent_role_name", ccs.getRole_name());
		}else{
			model.addAttribute("parent_role_name", "无");
		}
		// 根据角色id，查询出其菜单树
		CommonModel cs = (CommonModel)commonDao.getOneData("systemSql.getParentRole", c);
		if(cs !=null){
			c.setFdid(cs.getFdid());
		}
		JSONObject json = systemService.getMenuTreeJson(c);
		model.addAttribute("menuTreeJson",  json.toJSONString());
		model.addAttribute("role_id", id);
		return "system/editRolePage" ;
	}
	
	@RequestMapping("getEltCheckedOfRole/{roleId}/{menuId}")
	@ResponseBody
	public JSONObject getEltCheckedOfRole(@PathVariable(value="roleId") String roleId,
			@PathVariable(value="menuId") String menuId){
		JSONObject json = new JSONObject();
		CommonModel c = new CommonModel();
		c.setTemp_str1(roleId);
		c.setTemp_str2(menuId);
		c.setFdid(roleId);
		json = systemService.getEltCheckedOfRole(c);
		return json  ;
	}
	/****
	 * 保存更新角色
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
	
}
