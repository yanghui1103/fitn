package com.bw.fit.common.dao.impl;

import java.util.*;

import org.json.simple.JSONObject;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.bw.fit.common.dao.CommonDao;
import com.bw.fit.common.model.RbackException;

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
	public void insert(String sql, Object param) throws RbackException {
		int res=0;
		try {
			res = sqlSessionTemplate.insert(sql, param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RbackException("1","持久层执行失败，请联系系统管理员");
		}
		if(res<1)
			throw new RbackException("1","持久层执行失败，请联系系统管理员");
	}
	@Override
	public void update(String sql, Object param) throws RbackException {
		int res=0;
		try {
			res = sqlSessionTemplate.update(sql, param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RbackException("1","持久层执行失败，请联系系统管理员");
		}
		if(res<1)
			throw new RbackException("1","持久层执行失败，请联系系统管理员");
	}
	@Override
	public void delete(String sql, Object param) throws RbackException {
		int res=0;
		try {
			res = sqlSessionTemplate.delete(sql, param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RbackException("1","持久层执行失败，请联系系统管理员");
		}
		if(res<1)
			throw new RbackException("1","持久层执行失败，请联系系统管理员");
	}
}
