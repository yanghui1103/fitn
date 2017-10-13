package com.bw.fit.common.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;
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

import Decoder.BASE64Decoder;

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

	public static String getMACAddress(String ip) {
		String str = "";
		String macAddress = "";
		try {
			Process p = Runtime.getRuntime().exec("nbtstat -a " + ip);
			InputStreamReader ir = new InputStreamReader(p.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			for (int i = 1; i < 100; i++) {
				str = input.readLine();
				if (str != null) {
					// if (str.indexOf("MAC Address") > 1) {
					if (str.indexOf("MAC") > 1) {
						macAddress = str.substring(str.indexOf("=") + 2,
								str.length());
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		return macAddress;
	}

	/****
	 * 前者包含后者
	 * 
	 * @param longString
	 * @param str
	 * @return
	 */
	public static boolean isContains(String longString, String str) {
		if (longString != null && longString.contains(str)) {
			return true;
		}
		return false;
	}

	/** 将Base64 转换为file文件 */
	public static boolean base64ToFile(String base64, String path) {
		byte[] buffer;
		try {
			buffer = Base64.getDecoder().decode(base64);
			FileOutputStream out = new FileOutputStream(path);
			out.write(buffer);
			out.close();
			return true;
		} catch (Exception e) {
			throw new RuntimeException("base64字符串异常或地址异常\n" + e.getMessage());
		}
	}

	/** 将 file 转化为 Base64 */
	public static String fileToBase64(String path) {
		File file = new File(path);
		FileInputStream inputFile;
		try {
			inputFile = new FileInputStream(file);
			byte[] buffer = new byte[(int) file.length()];
			inputFile.read(buffer);
			inputFile.close();
			return Base64.getEncoder().encodeToString(buffer);
		} catch (Exception e) {
			throw new RuntimeException("文件路径无效\n" + e.getMessage());
		}
	}

	/*****
	 * 将一个数据扩充长度 只能比原来的长度更长
	 * 
	 * @param oldArray
	 * @param newSize
	 * @return
	 */
	private static Object resizeArray(Object oldArray, int newSize) {
		int oldSize = java.lang.reflect.Array.getLength(oldArray);
		Class elementType = oldArray.getClass().getComponentType();
		Object newArray = java.lang.reflect.Array.newInstance(elementType,
				newSize);
		int preserveLength = Math.min(oldSize, newSize);
		if (preserveLength > 0)
			System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
		return newArray;
	}

	public static boolean isImageFromBase64(String base64Str) {
		boolean flag = false;
		try {
			BufferedImage bufImg = ImageIO.read(new ByteArrayInputStream(
					new BASE64Decoder().decodeBuffer(base64Str)));
			if (null == bufImg) {
				return flag;
			}
			flag = true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return flag;
	}

	public static void targetZoomOut(String sourcePath) { // 将目标图片缩小成256*256并保存
		File file1 = new File(sourcePath); // 用file1取得图片名字
		String name = file1.getName();
		try {
			BufferedImage input = ImageIO.read(file1);
			Image big = input.getScaledInstance(256, 256, Image.SCALE_DEFAULT);
			BufferedImage inputbig = new BufferedImage(256, 256,
					BufferedImage.TYPE_INT_BGR);
			inputbig.getGraphics().drawImage(input, 0, 0, 256, 256, null); // 画图

			ImageIO.write(inputbig, "jpg", new File(PropertiesUtil.getValueByKey("system.attachment_path")
					+ name)); // 将其保存在C:/imageSort/targetPIC/下
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String strInput = "验证码不得为空";
		StringBuffer output = new StringBuffer();
		System.out.println("\"" + strInput + "\" 的utf8编码：");
		for (int i = 0; i < strInput.length(); i++) {
			output.append("\\u" + Integer.toString(strInput.charAt(i), 16));
		}
		System.out.println(output);
	}
}
