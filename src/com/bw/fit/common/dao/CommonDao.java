package com.bw.fit.common.dao;

import java.util.List;

import org.json.simple.JSONObject;

public interface CommonDao {
	public Object getOneData(String sql, Object param);
	public List getListData(String sql, Object param) ;
	public void insert(String sql, Object param) throws Exception ;
	public void update(String sql, Object param) throws Exception;
	public void delete(String sql, Object param) throws Exception;
}
