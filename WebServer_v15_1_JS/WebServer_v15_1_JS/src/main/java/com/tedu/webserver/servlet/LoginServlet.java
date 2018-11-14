package com.tedu.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;

import com.tedu.webserver.http.HttpRequest;
import com.tedu.webserver.http.HttpResponse;

/**
 * 处理登录业务
 * @author adminitartor
 *
 */
public class LoginServlet extends HttpServlet{
	public void service(HttpRequest request,HttpResponse response){
		try {
			//处理登录流程
			//1
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			
			//2
			try(
				RandomAccessFile raf = 
				new RandomAccessFile("user.dat","r");	
			){
				boolean check = false;
				for(int i=0;i<raf.length()/100;i++){
					//每次读取前先将指针移动到该条记录的开始位置
					raf.seek(i*100);
					
					//读取用户名
					byte[] data = new byte[32];
					raf.read(data);
					String name = new String(data,"UTF-8").trim();
					
					//判断是否为该用户
					if(name.equals(username)){
						//读取密码
						raf.read(data);
						String pwd = new String(data,"UTF-8").trim();
						if(pwd.equals(password)){
							//登录成功
							check = true;
							forward("/myweb/login_success.html", request, response);
							break;
						}
					}
				}
				if(!check){
					forward("/myweb/login_fail.html",request,response);
				}				
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}


