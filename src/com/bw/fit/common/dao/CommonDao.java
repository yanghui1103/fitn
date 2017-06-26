package com.bw.fit.common.dao;

import java.util.List;

import org.json.simple.JSONObject;

import com.bw.fit.common.model.RbackException;

public interface CommonDao {
	public Object getOneData(String sql, Object param);
	public List getListData(String sql, Object param) ;
	public void insert(String sql, Object param) throws RbackException ;
	public void update(String sql, Object param) throws RbackException;
	public void delete(String sql, Object param) throws RbackException;
}
