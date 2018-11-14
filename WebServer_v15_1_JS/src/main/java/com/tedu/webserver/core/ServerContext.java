package com.tedu.webserver.core;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 服务端环境信息
 * 
 * @author adminitartor
 *
 */
public class ServerContext {
	/**
	 * 请求与Servlet映射。
	 * key:请求路径
	 * value:对应Servlet的名字
	 */
	private static Map<String,String> servletMapping = new HashMap<String,String>();
	
	static{
		initServletMapping();
	}
	
	/**
	 * 初始化请求与Servlet映射信息
	 */
	private static void initServletMapping(){
		/*
		 * 读取conf/server.xml文件
		 * 将所有<servlet>标签解析出来，用其中的
		 * url属性值作为key，className属性的值作为
		 * value存入到servletMapping中
		 */
		try {
			
			SAXReader reader = new SAXReader();
			
			Document doc = reader.read(new File("conf/server.xml"));
			Element root = doc.getRootElement();
			//获取servlets标签
			Element servlets = root.element("servlets");
			//获取所有servlet标签
			List<Element> servletList = servlets.elements();
			//遍历每个servlet标签
			for(Element servletEle : servletList){
				String key = servletEle.attributeValue("url");
				String value = servletEle.attributeValue("className");
				servletMapping.put(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		servletMapping.put("/myweb/reg", "com.tedu.webserver.servlet.RegServlet");
//		servletMapping.put("/myweb/login", "com.tedu.webserver.servlet.LoginServlet");
	}
	
	/**
	 * 根据请求获取对应的Servlet名字
	 * @param url
	 * @return
	 */
	public static String getServletName(String url){
		return servletMapping.get(url);
	}

	public static void main(String[] args) {
		String servletName = getServletName("/myweb/login");
		System.out.println(servletName);
	}
}






