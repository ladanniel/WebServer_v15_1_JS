package com.tedu.webserver.servlet;

import java.io.RandomAccessFile;
import java.util.Arrays;

import com.tedu.webserver.http.HttpRequest;
import com.tedu.webserver.http.HttpResponse;

/**
 * 处理注册业务
 * @author adminitartor
 *
 */
public class RegServlet extends HttpServlet{
	public void service(HttpRequest request,HttpResponse response){
		try {
			System.out.println("开始处理注册业务！！");
			/*
			 * 注册业务流程
			 * 1:通过request获取用户在注册页面上输入的
			 *   注册信息
			 * 2:使用RandomAccessFile打开user.dat文件
			 * 3:将该用户信息写入文件
			 * 4:设置response，响应注册成功页面。 
			 */
			//1
			String username = request.getParameter("username");
			System.out.println("username======"+username);
			String password = request.getParameter("password");
			System.out.println("password===="+password);
			String nickname = request.getParameter("nickname");
			int age = Integer.parseInt(request.getParameter("age"));
			System.out.println("看看age有没有输出-----------------》"+age);
			//还要对用户数据进行必要验证
			
			//2
			try(
				RandomAccessFile raf 
					= new RandomAccessFile("user.dat","rw");
			){
				raf.seek(raf.length());
				//写用户名
				byte[] data = username.getBytes("UTF-8");
				data = Arrays.copyOf(data, 32);
				raf.write(data);
				
				//写密码
				data = password.getBytes("UTF-8");
				data = Arrays.copyOf(data, 32);
				raf.write(data);
				
				//写昵称
				data = nickname.getBytes("UTF-8");
				data = Arrays.copyOf(data, 32);
				raf.write(data);
				
				//写年龄
				raf.writeInt(age);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//3响应客户端注册成功页面
			/*
			 * 设置重定向路径注意，若使用相对路径，相对的应当是
			 * 请求当前servlet时路径
			 * 例如：球球regServlet时，客户端路径为：
			 * http://localhost:8080/myweb/reg
			 * 那么当前目录就是
			 * http://localhost:8080/myweb/
			 * 所以设置重定向目录只需要设置为
			 * reg_success。html即可
			 * 那么浏览器得到这个路径后会拼在当前目录中
			 *   http://localhost:8080/myweb/reg_success.html
			 */
			forward("/myweb/reg_success.html", request, response);		
			response.sendRedirect("reg_success.html");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

//问题总结，注册不成功有以下几个原因：刚开始写可能路径没有改为getRequest.URI,
//后期出现空指针异常，多半原因是response中的content中没有加if(entity!=null)的原因导致的，
//后面出现的格式错误，可能是在拆分功能的时候出现错误







