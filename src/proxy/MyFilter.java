package proxy;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tool.WrapperResponse;
import tool.WrapperedResponse;
import tool.ZipUtils;
import tool.MyHTTPUtils;


public class MyFilter implements Filter {
	
	private FilterConfig filterConfig;

	@Override
	public void destroy(){
		this.filterConfig = null;
	}
	 
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
			{
		HttpServletResponse httpResponse = (HttpServletResponse)response;
//		WrapperResponse httpResponse = new WrapperResponse((HttpServletResponse)response);
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		if(httpRequest.getHeader("host").equalsIgnoreCase("localhost")||
				httpRequest.getHeader("host").equalsIgnoreCase("127.0.0.1")){
			System.out.println("host is localhost, return from filter");
			return;
		}
		
		String requestMethod = httpRequest.getMethod();
		String urlStr=httpRequest.getRequestURL().toString();
		String queryStr = httpRequest.getQueryString();
		 
		System.out.println("request : "+requestMethod+"---"+urlStr);
		if(queryStr != null && !"".equals(queryStr)){
			urlStr = urlStr +"?"+ queryStr;
		}
		URLConnection urlConnection;
		HttpURLConnection httpUrlConnection = null;
		try {
			urlConnection = new URL(urlStr).openConnection();
			httpUrlConnection = (HttpURLConnection) urlConnection;
			httpUrlConnection.setConnectTimeout(30000);
			httpUrlConnection.setReadTimeout(30000);
		} catch (IOException e2) {
System.err.println("open " + urlStr + " failed...........................................");
			e2.printStackTrace();
			try {
				httpResponse.sendError(500);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		
		Enumeration<String> requestHeaders = httpRequest.getHeaderNames();
		while(requestHeaders.hasMoreElements()){
			String header = requestHeaders.nextElement();
			String headerValue = httpRequest.getHeader(header);
			httpUrlConnection.setRequestProperty(header, headerValue);
		}
		
		httpUrlConnection.setDoInput(true);
		httpUrlConnection.setUseCaches(false);
		
		try {
			httpUrlConnection.setRequestMethod(requestMethod);
		} catch (ProtocolException e2) {
			e2.printStackTrace();
		}
		//post method
		if("POST".equalsIgnoreCase(requestMethod)){
			httpUrlConnection.setDoOutput(true);
			//read in-request body, write to reqeust-out
			OutputStream out;
			try {
				out = new BufferedOutputStream(httpUrlConnection.getOutputStream());
				int len = httpRequest.getContentLength();
				ServletInputStream sis = httpRequest.getInputStream();
				if(len>=0){
					byte[] message = new byte[len];
					sis.read(message, 0, len);
					out.write(message);
					out.close();
				} 
			} catch(IOException e){
System.out.println("unknownhost : " + httpUrlConnection.getURL().toString());
System.out.println("IOException occurred when httpUrlConnection.getOutputStream() exception class is : >>>>>>>>>>>>>>>>>" + e.getClass());
				try {
					httpResponse.sendError(404, "unknownhost exception");
				} catch (IOException e1) {
System.err.println("send error failed............................................");
					return;
				}
				return;
			}
		}else if("GET".equalsIgnoreCase(requestMethod)){//get method
			//do nothing
		}
		
		try {
			httpUrlConnection.connect();//calling #connect() will set request headers, therefore 
										//do not set header after #connect() is called
		} catch (IOException e) {//connection refused may occur here
System.out.println("IOException occurred when httpUrlConnection.connect() "+urlStr+" ...exception class is : >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + e.getClass());
System.out.println(e.getMessage());
			MyHTTPUtils.sendError(httpResponse,408, "请求超时");
			return;
		} 
		
		InputStream responseInputStream = null;
		Map<String, String> responseHeaders = null;
		int responseCode = -1;
		try {
			responseCode = httpUrlConnection.getResponseCode();//Connection refused: connect
System.out.println("response : " + responseCode + "---" + httpUrlConnection.getURL().toString());
			if(responseCode >= 400){
System.out.println("response code is >>>>>>>>>>>>>" + responseCode + "<<<<<<<<<<<<<<<<<<<<<<<<<<< return from this request");
				httpResponse.setStatus(responseCode);
				return;
			} 
			responseInputStream = httpUrlConnection.getInputStream();//
			responseHeaders = MyHTTPUtils.getHttpResponseHeader(httpUrlConnection);
			httpResponse.setStatus(responseCode);//set status code before write the response body
			MyHTTPUtils.setResponseHeader(httpResponse, responseHeaders);
			MyHTTPUtils.setResponseBody(httpResponse, responseInputStream);
			
		}catch (IOException e) {
System.out.println("httpUrlConnection concect failed...exception class is :>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + e.getClass());
			String exceptionClass = e.getClass().getName();
			MyHTTPUtils.sendError(httpResponse, 500, exceptionClass+" occurred");
		}

		httpUrlConnection.disconnect();
//		chain.doFilter(httpRequest, httpResponse);
		
	}
	
	@Override
    public void init(FilterConfig config) throws ServletException {  
        this.filterConfig = config;
		System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
		System.setProperty("sun.net.client.defaultReadTimeout", "30000");
    }

}
