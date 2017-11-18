package com.bw.fit.system.service;
import java.util.List;
import java.util.Map;

import com.bw.fit.system.model.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.bw.fit.common.model.BaseModel;
import com.bw.fit.common.model.CommonModel;
import com.bw.fit.common.model.LogUser;
import com.bw.fit.common.model.RbackException;
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
	public JSONObject getUserCheckResult(LogUser user);
	public String mmUserPassword(String staff_number,String passwd); /**得到加密后的密文*/
	public JSONObject getMenuTreeJsonByStaffId(CommonModel c);
	public JSONObject updatePwd(CommonModel c) throws RbackException;
	public List<CommonModel> getChildCompByCurrentComp(String fdid);
	public List<CommonModel> getChildCompsByThisComp(String fdid);
	public List<CommonModel> getCompanyList(CommonModel c);
	public List<CommonModel> getDataDictList(CommonModel c);
	public JSONObject getOperationsByMenuId(CommonModel c);
	public List<CommonModel> getAuthortiesByStaff(CommonModel c);
	public List<CommonModel> getDictInfo(CommonModel c); 
	public void createCompany(CommonModel c) throws RbackException;
	public void fillCommonField(BaseModel c,HttpSession session,boolean useFdid); // 装配，公共字段
	public List<CommonModel> getuserList(CommonModel c);
	public List<CommonModel> getstaffGrpList(CommonModel c);
	public List<CommonModel> getroleList(CommonModel c);
	public List<CommonModel> getpostionList(CommonModel c);
	public List<CommonModel> getCommonList(CommonModel c);
	public CommonModel getOneCommnonData(CommonModel c);
	public void insert(CommonModel c) throws RbackException;
	public void update(CommonModel c) throws RbackException;
	public void delete(CommonModel c) throws RbackException;
	public List<CommonModel> getObjByKeyWds(CommonModel c,String objStr);
	public void insertTempRelation(CommonModel c) throws RbackException;
	public void createStaff(Staff staff) throws RbackException;
	/****
	 * 修改密码
	 */
	public String getPasswdMMOfStaff(String staff_number,String password,String header);
	public void updateStaff(Staff staff) throws RbackException;
	/**
	 * 根据用户id查询角色ids和角色名nams；
	 * 岗位ids names
	 * 用户组ids names
	 */
	public Map<String,CommonModel> getRlPostGrpInfosByStaffId(String staff_id);
	public void createStaffGrp(CommonModel c) throws RbackException;
	public void deleteStaffGroup(CommonModel c) throws RbackException;
	public CommonModel getDetailsOfStaffGrp(CommonModel c);
	public void updateStaffGrp(CommonModel c)  throws RbackException;
	public JSONObject createPostion(CommonModel c) throws RbackException;
	public void delPostion(CommonModel c) throws RbackException;
	public CommonModel getDetailsOfPostion(CommonModel c);
	public void updatePostion(CommonModel c) throws RbackException;
	// 整个权限树
	public List<CommonModel> getAuthTreeAll(CommonModel c);
	// 获取某一个角色所拥有的全部权限单元
	public List<CommonModel> getAuthTreeOfRole(CommonModel c);
	public JSONObject getAuthTreeOfMyRole(CommonModel c);
	/***校验用户的角色列表是否包含‘系统管理权限’，方便不予拦截**/
	public boolean hasTopRoleSysStaff(HttpSession session);
	/**新建角色***/
	public void createRole(Role role ) throws RbackException;
	/***角色，查询出菜单树***/
	public JSONObject getMenuTreeJson(CommonModel c) ;
	JSONObject getEltCheckedOfRole(CommonModel c);
	void updateRole(CommonModel c) throws RbackException;
	void removeAuthority2menu(CommonModel c) throws RbackException;
	void delDataDict(CommonModel c) throws RbackException;
	void addDataDict(CommonModel c) throws RbackException;
	void updateDataDict(CommonModel c) throws RbackException;
	void createAttment(Attachment c) throws RbackException;
	List getWaitTodoList(String staff_id,String itemId,String readOrDo,String keyWords);
}
