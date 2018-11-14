package com.tedu.webserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * WebServer主类
 * @author adminitartor
 *
 */
public class WebServer {
	private ServerSocket server;
	private ExecutorService threadPool;
	
	public WebServer(){
		try {
			//Tomcat默认开启的端口就是8080
			server = new ServerSocket(8080);
			threadPool=Executors.newFixedThreadPool(50);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void start(){
		try {
			while(true){
				System.out.println("等待客户端连接...");
				Socket socket = server.accept();
				System.out.println("一个客户端连接了!");
				
				//启动一个线程，处理该客户端请求
				ClientHandler handler = new ClientHandler(socket);
				threadPool.execute(handler);
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	public static void main(String[] args) {
		WebServer server = new WebServer();
		server.start();
	}
}

















