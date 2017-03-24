package tool;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpProxy {

	private static final String SERVER_POST="POST";
	private static final String SERVLET_GET = "GET";
	//post访问方法
	 public  static HttpURLConnection doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
		 Enumeration<String> e = null;
		 Enumeration<String> headers = null;
		 if(request != null){
			 e =  request.getParameterNames();
			 headers = request.getHeaderNames();
		 }else{
			 System.out.println("request cannot be null!");
			 return null;
		 }
		 StringBuffer param=new StringBuffer();
		 StringBuffer connUrl = request.getRequestURL();
	   
	   //获取页面表单信息
	     while(e.hasMoreElements()) 
	          { 
	              String name=(String)e.nextElement();
	              String namepar[]=request.getParameterValues(name); 
	              for(int i=0;i<namepar.length;i++){
	                param.append(name).append("=").append(namepar[i]).append("&"); 
	               }
	          }
	     if(param.length()!=0){
	       param.deleteCharAt(param.length()-1);//去除最后一个&符号
	     }
	     
		   URL url=new URL(connUrl.toString());
		   HttpURLConnection conn=(HttpURLConnection)url.openConnection();
		   while(headers.hasMoreElements()){
				String header = headers.nextElement();
				String headerValue = request.getHeader(header);
				conn.setRequestProperty(header, headerValue);
		   }
		//	   conn.setRequestProperty("Content-Type","text/html; charset=UTF-8");  
		//	   conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		   conn.setRequestMethod(SERVER_POST);
		   conn.setDoInput(true);
		   conn.setDoOutput(true);
		   conn.connect(); 
		   //往代理服务端传递信息
		   DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		   out.write(param.toString().getBytes("utf-8"));
		   out.flush();
		   out.close();
		   return conn;
	 }
	 
	//get访问方法
	 public  static HttpURLConnection doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
		  Enumeration<String> e=  request.getParameterNames();
		  Enumeration<String> headers = request.getHeaderNames();
		  
		  StringBuffer param=new StringBuffer();
		  StringBuffer connUrl = request.getRequestURL();
		  
		  while(e.hasMoreElements()){ 
		        String name=(String)e.nextElement();
		        String namepar[]=request.getParameterValues(name); 
		        for(int i=0;i<namepar.length;i++){
		          param.append(name).append("=").append(namepar[i]).append("&"); 
		        }
		   }
		   if(param.length()!=0){
		     param.deleteCharAt(param.length()-1);//去除最后一个&符号
		   }
		   
		   URL url=new URL(connUrl+"?"+param.toString());
		   HttpURLConnection conn=(HttpURLConnection)url.openConnection();
		   
		   while(headers.hasMoreElements()){
				String header = headers.nextElement();
				String headerValue = request.getHeader(header);
				conn.setRequestProperty(header, headerValue);
		   }
		   conn.setRequestMethod(SERVLET_GET);  
//		   conn.setRequestProperty("Content-Type","text/html; charset=UTF-8");  
//		   conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		   conn.setDoInput(true);
		   conn.setDoOutput(true);
		   conn.connect();
		   
		   return conn;
	 }
	 
	//执行方法，传入request和response，以及访问的Url
	 public static byte[] exec(HttpServletRequest request, HttpServletResponse response,String connUrl) throws Exception{ 
	  HttpURLConnection conn=null;
	  if(request.getMethod().equalsIgnoreCase(SERVER_POST)){
	    conn=doPost(request, response);
	  }
	  else{
	    conn=doGet(request, response);
	  }
	  byte[] b= readInputStream(conn.getInputStream());
	  response.getOutputStream().write(b);
	  response.getOutputStream().flush();
	  response.getOutputStream().close();
	  conn.disconnect();
	  return b;
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
