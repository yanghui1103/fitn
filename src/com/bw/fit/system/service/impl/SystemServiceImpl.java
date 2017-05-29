package com.bw.fit.system.service.impl;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
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

	@Override
	public JSONObject getMenuTreeJsonByStaffId(CommonModel c) {
		JSONObject json =new JSONObject();
		List<CommonModel> list = new ArrayList<CommonModel>();
		List<CommonModel> list1 = (ArrayList<CommonModel>)commonDao.getListData("systemSql.getMenuOfStaff", c);
		List<CommonModel> list2 = (ArrayList<CommonModel>)commonDao.getListData("systemSql.getMenuOfStaffRole", c);
		List<CommonModel> list3 = (ArrayList<CommonModel>)commonDao.getListData("systemSql.getMenuOfStaffGrp", c);
		list.addAll(list1);
		list.addAll(list2);
		list.addAll(list3);
		if(list==null||list.size()<1){
			json.put("res", "1");
			json.put("msg", "此用户暂无菜单权限");
			return json ;
		}
		list = list.stream().distinct().sorted((n1,n2)->n1.getFdid().compareTo(n2.getFdid())).collect(Collectors.toList()); //去重
		// 拼接 start
		List<CommonModel> inNodeList = new ArrayList<>();
        JSONArray array = new JSONArray();
        for (int i=0;i<list.size() && !(inNodeList.contains(list.get(i)));i++){
        	CommonModel node = list.get(i);        
            JSONArray array2 = new JSONArray(); 
            for(int j=i+1;j<list.size();j++){
                if(node.getFdid() .equals( list.get(j).getParent_id())){  
                    JSONObject json2 =new JSONObject();
                    json2.put("id", list.get(j).getFdid());
                    json2.put("level", list.get(j).getMenu_level());
                    json2.put("page_name", list.get(j).getMenu_name());
                    json2.put("page_path", list.get(j).getMenu_path());
                    json2.put("p_id", list.get(j).getParent_id());
                    json2.put("rel", "page"+list.get(j).getFdid());
                    json2.put("default_action", list.get(i).getParams());
                    json2.put("page_url", list.get(j).getMenu_url());
                    array2.add(json2);
                    inNodeList.add(list.get(j));
                    JSONArray array3 = new JSONArray(); 
                    for(int k=j+1;k<list.size();k++){
                        if(list.get(j).getFdid() .equals( list.get(k).getParent_id())){  
                            JSONObject json3 =new JSONObject();
                            json3.put("id", list.get(k).getFdid());
                            json3.put("level", list.get(k).getMenu_level());
                            json3.put("page_name", list.get(k).getMenu_name());
                            json3.put("page_path", list.get(k).getMenu_path());
                            json3.put("p_id", list.get(k).getParent_id());
                            json3.put("rel", "page"+list.get(k).getFdid());
                            json3.put("default_action", list.get(k).getParams());
                            json3.put("page_url", list.get(k).getMenu_url());
                            array3.add(json3);
                            inNodeList.add(list.get(k));
                        }
                    }

                    if(array3.size()>0)
                        json2.put("childs",array3);
                }
            }
            JSONObject json1 = new JSONObject();
            json1.put("id", node.getFdid());
            json1.put("level", node.getMenu_level());
            json1.put("page_name", node.getMenu_name());
            json1.put("page_path", node.getMenu_path());
            json1.put("p_id", node.getParent_id());
            json1.put("rel", "page"+node.getFdid() );
            json1.put("default_action", node.getParams());
            json1.put("page_url", node.getMenu_url());
            if(array2.size()>0)
                json1.put("childs",array2); 
            array.add(json1);
        }
        json.put("list", array);		
		// 拼接End		
		
		json.put("res", "2");
		json.put("msg", "此用户拥有菜单权限");
		return json ;
	}
 
	
}
