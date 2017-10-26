package com.bw.fit.system.servlet;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bw.fit.common.dao.CommonDao;
import com.bw.fit.common.model.CommonModel;

@Component
public class MessageServlet extends HttpServlet{

	@Autowired
	private static CommonDao commonDao;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException { 

		JSONObject json = new JSONObject();
		CommonModel c = new CommonModel();
		c.setFdid("1111111111");
		CommonModel cc = (CommonModel)commonDao.getOneData("systemSql.getOneDictDetail",c);
		json.put("res", cc.getCan_add());
		
		System.out.println(cc.getCan_add());
    }
}
