package tool;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class MyHTTPUtils {

	public static void printRequest(HttpServletRequest httpRequest){
		System.out.println("-------------------request below----------------------");
		Enumeration<String> headers = httpRequest.getHeaderNames();
//		System.out.println("httpRequest.getScheme() = "+httpRequest.getScheme());
		System.out.println("httpRequest.getRequestURL() = " + httpRequest.getRequestURL());
		System.out.println("httpRequest.getMethod() = " + httpRequest.getMethod());
//		System.out.println("httpRequest.getContentLength() = " + httpRequest.getContentLength());
//		System.out.println("httpRequest.getContextPath() = " + httpRequest.getContextPath());
//		System.out.println("httpRequest.getLocalAddr() = " + httpRequest.getLocalAddr());
//		System.out.println("httpRequest.getServletPath() = " + httpRequest.getServletPath());
//		System.out.println("httpRequest.getRemoteAddr() = " + httpRequest.getRemoteAddr());
//		System.out.println("httpRequest.getRemoteAddr() = " + httpRequest.getRemoteHost());
//		System.out.println("httpRequest.getRequestURI() = " + httpRequest.getRequestURI());
//		System.out.println("httpRequest.getCharacterEncoding() = " + httpRequest.getCharacterEncoding());
//		System.out.println("httpRequest.getQueryString() = " + httpRequest.getQueryString());
//		System.out.println("httpRequest.getContentType() = " + httpRequest.getContentType());
//		System.out.println("httpRequest.getRequestedSessionId() = " + httpRequest.getRequestedSessionId());
//		System.out.println("httpRequest.getProtocol() = " + httpRequest.getProtocol());
//		System.out.println("httpRequest.getAuthType() = " + httpRequest.getAuthType());
//		System.out.println("httpRequest.getServerName() = " + httpRequest.getServerName());
		System.out.println("----------------------request headers---------");

		while(headers.hasMoreElements()){
			String header = headers.nextElement();
			String headerValue = httpRequest.getHeader(header);
			System.out.println(header + " : " + headerValue);
		}
		
		System.out.println("-----------------request parameters---------");
        Map<String, String>map = new HashMap<String, String>();  
        Enumeration<String> paramNames = httpRequest.getParameterNames();  
        while (paramNames.hasMoreElements()) {  
            String paramName = paramNames.nextElement();  
  
            String[] paramValues = httpRequest.getParameterValues(paramName);  
            for(String param : paramValues){
            	System.out.println(paramName + " : " + param);
            }
        }  
//  
//        Set<Map.Entry<String, String>> set = map.entrySet();  
//        System.out.println("------------------------------");  
//        for (Map.Entry entry : set) {  
//            System.out.println(entry.getKey() + ":" + entry.getValue());  
//        }  
//        System.out.println("------------------------------");
        System.out.println("-------------------request above----------------------");
	}
	
	public void commonPrint(HttpServletRequest request,
			HttpServletResponse response) {

		// 设置将字符以"UTF-8"编码输出到客户端浏览器
		response.setCharacterEncoding("UTF-8");
		// 通过设置响应头控制浏览器以UTF-8的编码显示数据，如果不加这句话，那么浏览器显示的将是乱码
		response.setHeader("content-type", "text/html;charset=UTF-8");
		
		String requestUrl = request.getRequestURL().toString();
		String requestUri = request.getRequestURI();
		String queryString = request.getQueryString();
		String remoteAddr = request.getRemoteAddr();
		String method = request.getMethod();
		String localAddr = request.getLocalAddr();
		String localName = request.getLocalName();
		String remoteHost = request.getRemoteHost();
		int remotePort = 0;
		PrintWriter out;
		try {
			out = response.getWriter();

			out.write("<hr/>");
			out.write("（1）获取客户机信息：");
			out.write("<br/>");
			out.write("请求的URL地址：" + requestUrl);
			out.write("<br/>");
			out.write("请求的资源：" + requestUri);
			out.write("<br/>");
			out.write("请求的URL地址中附带的参数：" + queryString);
			out.write("<br/>");
			out.write("来访者的IP地址：" + remoteAddr);
			out.write("<br/>");
			out.write("来访者的主机名：" + remoteHost);
			out.write("<br/>");
			out.write("使用的端口号：" + remotePort);
			out.write("<br/>");
			out.write("请求的方式：" + method);
			out.write("<br/>");
			out.write("服务器的IP地址：" + localAddr);
			out.write("<br/>");
			out.write("服务器的主机名：" + localName);

			// 获取的客户机的请求头信息
			out.write("<br/>");
			out.write("<hr/>");
			out.write("（2）获取客户机的请求头信息：");
			out.write("<br/>");

			// 得到所有请求头名组成的Enumeration
			Enumeration<String> headerNames = request.getHeaderNames();

			// 循环
			while (headerNames.hasMoreElements()) {

				// 得到所有的请求头名
				String headerName = headerNames.nextElement();
				// 根据请求头名得到所有请求头值
				String headerValue = request.getParameter(headerName);
				out.write(headerName + ": " + headerValue);
				out.write("<br/>");
			}

			// 获取的客户机的请求信息
			out.write("<br/>");
			out.write("<hr/>");
			out.write("（3）获取客户机的请求信息：");
			out.write("<br/>");

			// 获取参数名和参数值的String[]组成的键值对
			Map<String, String[]> map = request.getParameterMap();
			Set<Entry<String, String[]>> entrySet = map.entrySet();
			for (Entry<String, String[]> entry : entrySet) {

				out.write(entry.getKey() + "： "
						+ Arrays.asList(entry.getValue()));
				out.write("<br/>");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	public static void setResponseHeader(HttpServletResponse hres, Map<String,String> headers) 
			throws UnsupportedEncodingException{

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            hres.setHeader(key, value);
        }
//        return hres;
	}
	
	public static void setResponseBody(HttpServletResponse httpResponse, InputStream ins){
		byte[] response_in;
		try {
			response_in = readInputStream(ins);
			if(response_in == null) return;
			httpResponse.getOutputStream().write(response_in);//先写 header 再写 body
			httpResponse.getOutputStream().flush();
			httpResponse.getOutputStream().close();
		} catch (IOException e) {
			response_in = null;
System.out.println("IOException occurred in setResponseBody,e.getMessage() is >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+e.getMessage());
			return;
		}finally{
			if(ins != null){
				try {
					ins.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void printResponseHeader(HttpURLConnection http) throws UnsupportedEncodingException {
	    System.out.println("---------------------resposne form HttpURLConnection------------------------"); 
		Map<String, String> header = getHttpResponseHeader(http);
	     for (Map.Entry<String, String> entry : header.entrySet()) {
	         String key = entry.getKey() != null ? entry.getKey() + ":" : "";
	         System.out.println(key + entry.getValue());
	     }
	     System.out.println("---------------------resposne form HttpURLConnection------------------------"); 
	}

	public static void printResponseBody(HttpServletResponse httpResponse, InputStream ins) {
		byte[] responseBody = new byte[10240];
		InputStream is = null;
		try {
//			is = httpUrlConnection.getInputStream();
			responseBody = readInputStream(ins);
			String contentEncoding = httpResponse.getHeader("Content-Encoding");
			if(contentEncoding != null){
				if(contentEncoding.equalsIgnoreCase("gzip")){
					System.out.println(new String(ZipUtils.unGZip(responseBody),httpResponse.getCharacterEncoding()));
				}else {
					System.out.println("Content Encoding is  ----------->" + contentEncoding);
				}
				
			}else{
				System.out.println("Content Encoding is  ----------->" + contentEncoding);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(new String(response_in));
	}
	
	public static Map<String, String> getHttpResponseHeader(
	        HttpURLConnection http) throws UnsupportedEncodingException {
	    Map<String, String> header = new LinkedHashMap<String, String>();
	    for (int i = 0;; i++) {
	        String mine = http.getHeaderField(i);
	        if (mine == null)
	            break;
	        header.put(http.getHeaderFieldKey(i), mine);
	    }
	    return header;
	}

	 //读取输入流
	public static byte[] readInputStream(InputStream inStream)  {
	        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	        byte[] buffer = new byte[1024];
	        int len = -1;
	        byte[] data = {};
	        try {
				while( (len = inStream.read(buffer)) !=-1 ){
				    outStream.write(buffer, 0, len);
				}
				data = outStream.toByteArray();//形成网页的流
				outStream.close();
				inStream.close();
			} catch (IOException e) {
System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>ioexception occurred when readInputStream from InputStream<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
				e.printStackTrace();
				return null;
			}
	        return data;
	    }

}
