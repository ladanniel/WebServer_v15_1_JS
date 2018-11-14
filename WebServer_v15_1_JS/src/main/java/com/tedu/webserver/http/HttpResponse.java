package com.tedu.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 鍝嶅簲瀵硅薄 璇ョ被鐨勬瘡涓疄渚嬬敤浜庤〃绀轰竴涓湇鍔＄鍙戦�佺粰瀹㈡埛绔殑 鍝嶅簲鍐呭
 * 
 * @author adminitartor
 *
 */
public class HttpResponse {
	private Socket socket;
	private OutputStream out;

	/*
	 * 鐘舵�佽鐩稿叧淇℃伅瀹氫箟
	 */
	// 鐘舵�佷唬鐮�
	private int statusCode;

	/*
	 * 鍝嶅簲澶寸浉鍏充俊鎭畾涔�
	 */
	private Map<String, String> headers = new HashMap<String, String>();

	/*
	 * 鍝嶅簲姝ｆ枃鐩稿叧淇℃伅瀹氫箟
	 */
	// 瑕佸搷搴旂殑瀹炰綋鏂囦欢
	private File entity;

	public HttpResponse(Socket socket) {
		try {
			this.socket = socket;
			this.out = socket.getOutputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 灏嗗搷搴斿唴瀹规寜鐓TTP鍗忚鏍煎紡鍙戦�佺粰瀹㈡埛绔�
	 */
	public void flush() {
		/*
		 * 鍝嶅簲瀹㈡埛绔仛涓変欢浜� 1:鍙戦�佺姸鎬佽 2:鍙戦�佸搷搴斿ご 3:鍙戦�佸搷搴旀鏂�
		 */
		sendStatusLine();
		sendHeaders();
		sendContent();
	}

	/**
	 * 鍙戦�佺姸鎬佽
	 */
	private void sendStatusLine() {
		try {
			String line = "HTTP/1.1" + " " + statusCode + " " + HttpContext.getStatusReason(statusCode);
			println(line);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 鍙戦�佸搷搴斿ご
	 */
	private void sendHeaders() {
		try {
			// 閬嶅巻headers锛屽皢鎵�鏈夋秷鎭ご鍙戦�佽嚦瀹㈡埛绔�
			Set<Entry<String, String>> set = headers.entrySet();
			for (Entry<String, String> header : set) {
				// 鑾峰彇娑堟伅澶寸殑鍚嶅瓧
				String name = header.getKey();
				// 鑾峰彇娑堟伅澶村搴旂殑鍊�
				String value = header.getValue();
				String line = name + ": " + value;
				println(line);
			}

			// 琛ㄧず鍝嶅簲澶撮儴鍒嗗彂閫佸畬姣�
			println("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 鍙戦�佸搷搴旀鏂�
	 */
	private void sendContent() {
		if (entity != null) {
			try (FileInputStream fis = new FileInputStream(entity);) {

				byte[] data = new byte[1024 * 10];
				int len = -1;
				while ((len = fis.read(data)) != -1) {
					out.write(data, 0, len);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 灏嗙粰瀹氬瓧绗︿覆鎸夎鍙戦�佺粰瀹㈡埛绔�(浠RLF缁撳熬)
	 * 
	 * @param line
	 */
	private void println(String line) {
		try {
			out.write(line.getBytes("ISO8859-1"));
			out.write(13);// written CR
			out.write(10);// written LF
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 浠庡畾鍚戝埌鎸囧畾璺緞
	 * 
	 * @param url
	 */
	public void sendRedirect(String url) {
		// 璁剧疆浠ｇ爜
		this.statusCode = 302;
		this.headers.put("location", url);
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public File getEntity() {
		return entity;
	}

	/**
	 * 璁剧疆鍝嶅簲鐨勫疄浣撴枃浠舵暟鎹� 璇ユ柟娉曚細鑷姩娣诲姞瀵瑰簲鐨勪袱涓搷搴斿ご: Content-Type涓嶤ontent-Length
	 * 
	 * @param entity
	 */
	public void setEntity(File entity) {
		this.entity = entity;
		/*
		 * 1:娣诲姞鍝嶅簲澶碈ontent-Length
		 */
		this.headers.put("Content-Length", entity.length() + "");
		/*
		 * 2:娣诲姞鍝嶅簲澶碈ontent-Type 2.1:鍏堥�氳繃entity鑾峰彇璇ユ枃浠剁殑鍚嶅瓧 2.2:鑾峰彇璇ユ枃浠跺悕鐨勫悗缂�鍚�
		 * 2.3:閫氳繃HttpContext鏍规嵁璇ュ悗缂�鍚嶈幏鍙栧埌 瀵瑰簲鐨凜ontent-Type鐨勫�� 2.4:鍚慼eaders涓缃鍝嶅簲澶翠俊鎭�
		 */
		// 2.1 渚嬪:xxx.png
		String name = entity.getName();
		// 2.2 png
		String ext = name.substring(name.lastIndexOf(".") + 1);
		// 2.3
		String type = HttpContext.getMimeType(ext);
		// 2.4
		this.headers.put("Content-Type", type);

	}

	/**
	 * 娣诲姞涓�涓搷搴斿ご
	 * 
	 * @param name
	 *            鍝嶅簲澶寸殑鍚嶅瓧
	 * @param value
	 *            鍝嶅簲澶村搴旂殑鍊�
	 */
	public void putHeaders(String name, String value) {
		this.headers.put(name, value);
	}

}
