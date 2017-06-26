package com.bw.fit.common.model;

public class RbackException extends Exception {

	/****
	 * servi层事务回滚
	 */
	private String res ;
	private String msg ;
	public RbackException(String res,String msg){
		this.res = res;
		this.msg = msg;
	}
	public String getRes() {
		return res;
	}
	public void setRes(String res) {
		this.res = res;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
