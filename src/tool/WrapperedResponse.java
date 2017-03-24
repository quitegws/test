package tool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class WrapperedResponse extends HttpServletResponseWrapper {

	private HttpServletResponse response = null;
	private ByteArrayOutputStream buffer = null; 
	private ServletOutputStream out = null; 
	private PrintWriter writer = null; 
	
	public WrapperedResponse(HttpServletResponse response) throws IOException {
		super(response);
		this.response = response;
		buffer = new ByteArrayOutputStream();// 真正存储数据的流 
//		wos = new WapperedOutputStream(buffer); 
		out = this.response.getOutputStream();
		writer = new PrintWriter(new OutputStreamWriter(buffer, this.getCharacterEncoding())); 
	}

	@Override 
	public ServletOutputStream getOutputStream() throws IOException { 
		return out; 
	} 
	
	@Override
	public PrintWriter getWriter() throws UnsupportedEncodingException { 
		return writer; 
	} 
	
	@Override 
	public void flushBuffer() throws IOException { 
		if (out != null) { 
			out.flush(); 
		} 
		if (writer != null) { 
			writer.flush(); 
		} 
	} 
	
	@Override 
	public void reset() { 
		buffer.reset(); 
	} 
	
	/**将out、writer中的数据强制输出到WapperedResponse的buffer里面，否则取不到数据*/ 
	public byte[] getResponseData() throws IOException { 
		flushBuffer(); 
		return buffer.toByteArray(); 
	} 

//	
//	public class WapperedOutputStream{
//		private ByteArrayOutputStream bos = null; 
//		
//		public WapperedOutputStream(ByteArrayOutputStream stream) throws IOException { 
//			bos = stream; 
//		} 
//
//		public ByteArrayOutputStream getByteArrayOutputStream(){
//			if(bos != null){
//				return bos;
//			}
//			return null;
//		}
//		
//		public void write(int b) throws IOException { 
//			bos.write(b); 
//		} 
//		public void write(byte[] b) throws IOException { 
//			bos.write(b, 0, b.length); 
//		}
//	}
}
