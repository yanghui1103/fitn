package com.bw.fit.common.util;

import java.sql.Connection;

public class DbConnectionUtil {

	static Connection conn = null ;
	static {
		
	}
	public static Connection getConnection(){ 
		return conn ;
	}
}
