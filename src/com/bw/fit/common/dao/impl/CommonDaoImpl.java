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
	public int insert(String sql, Object param) throws Exception {
		int res = sqlSessionTemplate.insert(sql, param);
		return (res<1)?1:2;
	}
	@Override
	public int update(String sql, Object param) throws Exception {
		int res = sqlSessionTemplate.update(sql, param);
		return (res<1)?1:2;
	}
	@Override
	public int delete(String sql, Object param) throws Exception {
		int res = sqlSessionTemplate.delete(sql, param);
		return (res<1)?1:2;
	}
}
