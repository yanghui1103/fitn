package com.bw.fit.common.dao.impl;

import java.util.*;

import org.json.simple.JSONObject;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.bw.fit.common.dao.CommonDao;

@Repository
public class CommonDaoImpl implements CommonDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public Object getOneData(String sql, Object param) {
		Object obj = sqlSessionTemplate.selectOne(sql, param);
		return obj;
	}
	@Override
	public List getListData(String sql, Object param) {
		List list = (List) sqlSessionTemplate.selectList(sql, param);
		return list;
	}
	@Override
	public JSONObject insert(String sql, Object param) throws Exception {
		JSONObject obj = new JSONObject();
		int res = sqlSessionTemplate.insert(sql, param);
		if (res < 1) {
			obj.put("res", "1");
			obj.put("msg", "新增失败");
			return obj;
		}
		obj.put("res", "2");
		obj.put("msg", "新增成功");
		return obj;
	}
	@Override
	public JSONObject update(String sql, Object param) throws Exception {
		JSONObject obj = new JSONObject();
		int res = sqlSessionTemplate.update(sql, param);
		if (res < 1) {
			obj.put("res", "1");
			obj.put("msg", "更新失败");
			return obj;
		}
		obj.put("res", "2");
		obj.put("msg", "更新成功");
		return obj;
	}
	@Override
	public JSONObject delete(String sql, Object param) throws Exception {
		JSONObject obj = new JSONObject();
		int res = sqlSessionTemplate.delete(sql, param);
		if (res < 1) {
			obj.put("res", "1");
			obj.put("msg", "删除失败");
			return obj;
		}
		obj.put("res", "2");
		obj.put("msg", "删除成功");
		return obj;
	}
}
