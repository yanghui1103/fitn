package com.bw.fit.common.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.activiti.engine.form.FormProperty;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class PubFun {
	private static final double PI = 3.1415926535898;
	private static double EARTH_RADIUS = 6378.137;
	private static Log log = LogFactory.getLog(PubFun.class);

	public static String getSysDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	public static String getSysDateM() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(date);
	}

	public static String getTruncSysDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	public static boolean checkSessionValidate(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			return true;
		}

		return false;
	}

	public static String getFixLenthString(int strLength) throws Exception {

		Date d = new Date();

		return String.valueOf(d.getTime());
	}

	/*
	 * convertXml2JsonByResMsg
	 */
	public static String convertXml2JsonByResMsg(Document doc) {
		String json = "{\"res\":\"";
		json = json + doc.selectSingleNode("/root/res").getText();
		json = json + "\",\"msg\":\"";
		json = json + doc.selectSingleNode("/root/msg").getText();
		json = json + "\"}";
		return json;

	}

	public static String getMutilLongIntId() {
		return String.valueOf(System.currentTimeMillis());
	}

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	public static double GetDistance(double lat1, double lng1, double lat2,
			double lng2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);

		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	public static double[] getAround(double lat, double lon, int raidus) {

		Double latitude = lat;
		Double longitude = lon;

		Double degree = (24901 * 1609) / 360.0;
		double raidusMile = raidus;

		Double dpmLat = 1 / degree;
		Double radiusLat = dpmLat * raidusMile;
		Double minLat = latitude - radiusLat;
		Double maxLat = latitude + radiusLat;

		Double mpdLng = degree * Math.cos(latitude * (PI / 180));
		Double dpmLng = 1 / mpdLng;
		Double radiusLng = dpmLng * raidusMile;
		Double minLng = longitude - radiusLng;
		Double maxLng = longitude + radiusLng;
		return new double[] { minLat, minLng, maxLat, maxLng };
	}

	public static ClassPathXmlApplicationContext getSysSpringCtx() {
		ClassPathXmlApplicationContext ctx = null;
		{
			if (ctx == null) {
				ctx = new ClassPathXmlApplicationContext("springAppContext.xml");
			}
		}
		return ctx;
	}

	public static String xml2json(Document doc) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("res", doc.selectSingleNode("/root/res").getText());

		jsonObj.put(
				"msg",
				(doc.selectSingleNode("/root/msg").getText() == null || ""
						.equals(doc.selectSingleNode("/root/msg").getText())) ? ""
						: doc.selectSingleNode("/root/msg").getText());
		return jsonObj.toJSONString();
	}

	public static String getFileTypeName(String s) {
		int index = 0;
		for (int i = s.length() - 1; i > 0; i--) {
			if ('.' == (s.charAt(i))) {
				index = i;
				break;
			}
		}
		return (s.substring(index, s.length()));
	}

	public static String getUUID(boolean isContainMLine) {
		String s = UUID.randomUUID().toString();
		if (isContainMLine) {
			return s;
		}
		return s.replace("-", "");
	}

	public static String getUUID() {
		return getUUID(false);
	}

	public static Map<String, FormProperty> createMap(List<FormProperty> props) {
		Map<String, FormProperty> re = new HashMap<String, FormProperty>();
		for (FormProperty p : props) {
			re.put(p.getId(), p);
		}
		return re;
	}

	public static JSONArray getRequestParamData(HttpServletRequest request)
			throws Exception {
		String str = new String(
				(request.getParameter("context")).getBytes("ISO-8859-1"), "GBK");
		JSONObject obj = (JSONObject) JSONValue.parse(str);
		JSONArray array = (JSONArray) obj.get("content");
		return array;
	}

	public static int getPageStartNum(String pageNo, String tatol) {
		log.info(tatol);
		log.info(Integer.valueOf(tatol));
		return Integer.valueOf(pageNo) * Integer.valueOf(tatol);
	}

	public static int getPageEndNum(String pageNo, String tatol) {
		return Integer.valueOf(pageNo) * Integer.valueOf(tatol)
				+ Integer.valueOf(tatol);
	}

	public static int getPageTatolNum(int size, int page_size) {
		if (page_size <= 0) {
			return -9;
		}
		int zheng = size / page_size;
		return zheng + (size % page_size > 0 ? 1 : 0);
	}

	public static void main(String[] args) {
		System.out.println(2 / 20);
	}

	public static String getTruncDouleNumber(String s) {
		int i = s.indexOf(".");
		return (s.substring(0, i + 2));
	}

	public static String getTruncDouleNumber(Double s) {
		String s2 = String.valueOf(s);
		int i = s2.indexOf(".");
		return (s2.substring(0, i + 2));
	}

	public static HSSFCellStyle getStyleByCompLevel(HSSFWorkbook wb, int level) {
		HSSFCellStyle style = wb.createCellStyle();
		if (level == 6) {
			style.setFillForegroundColor(HSSFColor.RED.index);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		} else if (level == 8) {
			style.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		} else if (level == 10) {
			style.setFillForegroundColor(HSSFColor.GREEN.index);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		} else if (level == 12) {
			style.setFillForegroundColor(HSSFColor.AQUA.index);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		} else if (level == 14) {
			style.setFillForegroundColor(HSSFColor.YELLOW.index);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		}
		return style;
	}



	/**
	 * date2比date1多的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int differentDays(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		int day1 = cal1.get(Calendar.DAY_OF_YEAR);
		int day2 = cal2.get(Calendar.DAY_OF_YEAR);

		int year1 = cal1.get(Calendar.YEAR);
		int year2 = cal2.get(Calendar.YEAR);
		if (year1 != year2) // 同一年
		{
			int timeDistance = 0;
			for (int i = year1; i < year2; i++) {
				if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) // 闰年
				{
					timeDistance += 366;
				} else // 不是闰年
				{
					timeDistance += 365;
				}
			}

			return timeDistance + (day2 - day1);
		} else // 不同年
		{
			System.out.println("判断day2 - day1 : " + (day2 - day1));
			return day2 - day1;
		}
	}

	/**
	 * 这个主要用在查询条件返回时候 去掉-9
	 */
	public static String getCleanString(String s) {
		if ("-9".equals(s))
			return "";
		return s;
	}

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
