package com.tedu.webserver.servlet;


import java.io.RandomAccessFile;
import java.io.File;
import com.tedu.webserver.http.HttpRequest;
import com.tedu.webserver.http.HttpResponse;

public  class LoginServerlet extends HttpServlet {

	public void service(HttpRequest request,HttpResponse response){
		try{
           System.out.println("��ʼ������֤ҵ�񡢡�����������");
			String username = request.getParameter("username");
			String password = request.getParameter("password");

			try (RandomAccessFile raf = 
					new RandomAccessFile("user.dat","r");){
				boolean flag = false;
				for (int i = 0; i < raf.length()/100; i++) {
					raf.seek(i*100);

					byte [] data = new byte[32];
					raf.read(data);
					String str = new String(data,"GBK").trim();

					if (str.equals(username)) {
						raf.read(data);
						String pw = new String(data,"GBK").trim();
						if (pw.equals(password)) {
							flag= true;
							System.out.println("��½�ɹ���");
						
							forward("/myweb/login_success.html", request,response);
							
							break;
						}

					}
				}
					System.out.println("�������е����������������������������������");
					if(!flag){
						
						forward("/myweb/login_fail.html", request,response);
						System.out.println("��½ʧ��");

					


				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();


		}
	}
	
}