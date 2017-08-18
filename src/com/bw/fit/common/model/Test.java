package com.bw.fit.common.model;

import java.awt.List;

public class Test<T> {


	public static void main(String[] args) {
		String strInput ="帐号不能为空";
        StringBuffer output = new StringBuffer();
        System.out.println("\""+strInput+ "\" 的utf8编码：");
        for (int i = 0; i < strInput.length(); i++)
        {
            output.append("\\u" +Integer.toString(strInput.charAt(i), 16));
        }        
        System.out.println(output);
	}
}
