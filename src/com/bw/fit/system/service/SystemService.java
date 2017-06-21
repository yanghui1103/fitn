package com.bw.fit.system.service;
import java.util.List;

import com.bw.fit.system.model.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.bw.fit.common.model.CommonModel;
import com.bw.fit.common.model.LogUser;
import com.bw.fit.system.model.Staff;
import com.bw.fit.system.persistence.BaseConditionVO;

public interface SystemService {

	/****
	 * @author yangh
	 * @result 
	 */
	public JSONObject getOnLineSituation(HttpSession session,LogUser user,ServletContext servletContext);
	public Staff getStaffInfoByNumber(CommonModel c);
	public List<Role> getRoleListByStaffId(CommonModel c);
	public List<Postion> getPostionListByStaffId(CommonModel c);
	public JSONObject getPwdCheckResult(LogUser user);
	public String mmUserPassword(String staff_number,String passwd); /**得到加密后的密文*/
	public JSONObject getMenuTreeJsonByStaffId(CommonModel c);
	public JSONObject updatePwd(CommonModel c) throws Exception;
	public List<CommonModel> getChildCompByCurrentComp(String fdid);
	public List<CommonModel> getChildCompsByThisComp(String fdid);
	public List<CommonModel> getCompanyList(CommonModel c);
	public List<CommonModel> getDataDictList(CommonModel c);
	public JSONObject getOperationsByMenuId(CommonModel c);
	public List<CommonModel> getAuthortiesByStaff(CommonModel c);
	public List<CommonModel> getDictInfo(CommonModel c); 
	public void createCompany(CommonModel c) throws Exception;
	public void fillCommonField(CommonModel c,HttpSession session); // 装配，公共字段
}
