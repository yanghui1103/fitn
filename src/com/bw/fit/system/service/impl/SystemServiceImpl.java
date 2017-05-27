package com.bw.fit.system.service.impl;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bw.fit.common.dao.CommonDao;
import com.bw.fit.common.model.CommonModel;
import com.bw.fit.common.model.LogUser;
import com.bw.fit.common.service.impl.CommonServiceImpl;
import com.bw.fit.common.util.MD5;
import com.bw.fit.common.util.PropertiesUtil;
import com.bw.fit.system.lambda.SystemLambda;
import com.bw.fit.system.model.Postion;
import com.bw.fit.system.model.Role;
import com.bw.fit.system.model.Staff;
import com.bw.fit.system.service.SystemService;

@Service
public class SystemServiceImpl implements SystemService {

	@Autowired
	private CommonDao commonDao ;
	public String test() throws Exception {
		CommonServiceImpl c = new CommonServiceImpl();
		int g = (int) c.function(11, SystemLambda.f); //
		return String.valueOf(g);
	}

	@Override
	public JSONObject getOnLineSituation(HttpSession session, LogUser user,
			ServletContext servletContext) {
		List<LogUser> showList = (ArrayList) (servletContext
				.getAttribute("onLineUserList"));
		JSONObject json = new JSONObject();
		if(showList==null||showList.size()<1){
			json.put("res", "2");
			json.put("msg", "此帐号此IP可以登录使用");
			return json;
		}
		int LogUserMaxCnt = Integer.valueOf(PropertiesUtil
				.getValueByKey("user.login.maxcnt"));
		if (showList!=null && (showList.size() >= LogUserMaxCnt)) {
			json.put("res", "1");
			json.put("msg", "在线人数已经超出上限数目:" + LogUserMaxCnt);
			return json;
		}
		List<LogUser> afterList = showList
				.parallelStream()
				.filter((n) -> n.getUser_cd().equalsIgnoreCase(
						user.getUser_cd())
						&& n.getIp().equalsIgnoreCase(user.getIp()))
				.collect(Collectors.toList());
		if (afterList != null || afterList.size() > 0) {
			json.put("res", "1");
			json.put("msg", "此帐号已经在别的地方登录");
			return json;
		}
		CommonModel c = new CommonModel();
		commonDao.getListData("systemSql.getStaffInfoByNumber", c);
		json.put("res", "2");
		json.put("msg", "此帐号此IP可以登录使用");
		return json;
	}

	@Override
	public Staff getStaffInfoByNumber(CommonModel c) {
		List<Staff> list = (ArrayList<Staff>)commonDao.getListData("systemSql.getStaffInfoByNumber", c);
		if(list.size()!=1)
			return null ;
		return list.get(0);
	}

	@Override
	public List<Role> getRoleListByStaffId(CommonModel c) {
		List<Role> list = (ArrayList<Role>)commonDao.getListData("systemSql.getStaffInfoByNumber", c);
		
		return list;
	}

	@Override
	public List<Postion> getPostionListByStaffId(CommonModel c) {
		List<Postion> list = (ArrayList<Postion>)commonDao.getListData("systemSql.getPostionListByStaffId", c);
		
		return list;
	}

	@Override
	public JSONObject getPwdCheckResult(LogUser user) {
		JSONObject json = new JSONObject();
		StringBuffer smm = new StringBuffer();
		smm.append(PropertiesUtil.getValueByKey("user.pw.slogmm"));
		smm.append(user.getUser_cd());
		smm.append(user.getPasswd());
		CommonModel c= new CommonModel();
		c.setStaff_number(user.getUser_cd());
		Staff staff = getStaffInfoByNumber(c);
		if(staff==null){ // 用户不存在，或资料重复
			json.put("res", "1");
			json.put("msg", "用户不存在,或资料出错");
			return json ;
		}
		MD5 m = new MD5();
		if(!m.getMD5ofStr(smm.toString()).equals(staff.getPassword())){
			json.put("res", "1");
			json.put("msg", "密码有误");
			return json ;
		}
		json.put("res", "2");
		json.put("msg", "密码正确");
		return json;
	}
	/***
	 * 加密
	 */
	@Override
	public String mmUserPassword(String staff_number,String passwd) {
		StringBuffer smm = new StringBuffer();
		MD5 m = new MD5();
		smm.append(PropertiesUtil.getValueByKey("user.pw.slogmm"));
		smm.append(staff_number);
		smm.append(passwd);
		return m.getMD5ofStr(smm.toString());
	}
	

}
