package com.tedu.webserver.http;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 该类定义了HTTP协议相关信息
 * 
 * @author adminitartor
 *
 */
public class HttpContext {
	/**
	 * 状态代码与对应状态描述的映射关系 key:状态代码 value:状态描述
	 */
	private static Map<Integer, String> STATUS_REASON_MAPPING = new HashMap<Integer, String>();
	/**
	 * 资源后缀与Content-Type之间的映射关系 key:资源的后缀名 value:该资源对应的Content-Type的值
	 * 注:不同的资源对应的Content-Type的值在w3c上都有定义 ，可前往w3c官网查询MIME定义
	 * 
	 */
	private static Map<String, String> MIME_MAPPING = new HashMap<String, String>();

	static {
		initStatusReasonMapping();
		initMimeMapping();
	}

	/**
	 * 初始化资源后缀与Content-Type的映射Map
	 */
	private static void initMimeMapping() {
		/*
		 * 读取conf/web.xml文件，将根元素下所有 名为<mime-mapping>的子元素读取出来，
		 * 然后将每个<mime-mapping>元素中的子元素 <extension>中间的文本作为key,将子元素
		 * <mime-type>中间的文本作为value，存入 到MIME_MAPPING中，完成初始化。
		 */
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new File("conf/web.xml"));
			Element root = doc.getRootElement();
			List<Element> mimeList = root.elements("mime-mapping");
			for (Element mimeEle : mimeList) {
				String key = mimeEle.elementText("extension");
				String value = mimeEle.elementText("mime-type");
				MIME_MAPPING.put(key, value);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// MIME_MAPPING.put("html", "text/html");
		// MIME_MAPPING.put("png", "image/png");
		// MIME_MAPPING.put("gif", "image/gif");
		// MIME_MAPPING.put("jpg", "image/jpeg");
		// MIME_MAPPING.put("css", "text/css");
		// MIME_MAPPING.put("js", "application/javascript");
	}

	/**
	 * 初始化状态代码与描述的映射MAP
	 */
	private static void initStatusReasonMapping() {
		STATUS_REASON_MAPPING.put(200, "OK");
		STATUS_REASON_MAPPING.put(302, "Move Temporaily");
		STATUS_REASON_MAPPING.put(404, "Not Found");
		STATUS_REASON_MAPPING.put(500, "Internal Server Error");
	}

	/**
	 * 根据资源后缀名获取对应的Content-Type的值
	 * 
	 * @param ext
	 * @return
	 */
	public static String getMimeType(String ext) {
		return MIME_MAPPING.get(ext);
	}

	/**
	 * 根据给定的状态代码获取对应的状态描述
	 * 
	 * @param statusCode
	 * @return
	 */
	public static String getStatusReason(int statusCode) {
		return STATUS_REASON_MAPPING.get(statusCode);
	}

	public static void main(String[] args) {
		// 介质
		String type = getMimeType("js");
		System.out.println(type);
	}
}
