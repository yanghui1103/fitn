package com.bw.fit.common.dao;

import java.util.List;

import org.json.simple.JSONObject;

public interface CommonDao {
	public Object getOneData(String sql, Object param);
	public List getListData(String sql, Object param) ;
	public int insert(String sql, Object param) throws Exception ;
	public int update(String sql, Object param) throws Exception;
	public int delete(String sql, Object param) throws Exception;
}
