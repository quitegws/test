package proxy;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class FirstServlet
 */
@WebServlet("/FirstServlet")
public class FirstServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private int count = 0;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FirstServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Cookie[] cookie = request.getCookies();
		int visit=0;
		if(cookie != null){
			for(int i=0; (i<cookie.length) && (visit==0); i++){
				if(cookie[i].getName().equalsIgnoreCase("visit")){
					visit = Integer.parseInt(cookie[i].getValue());
				}
			}
		}
		visit++;
		Cookie ck = new Cookie("visit",new Integer(visit).toString());
		ck.setMaxAge(60*60);
		
		response.addCookie(ck);//addcookie 在写 response 之前
		
		response.getWriter().append("Served at: ").append(request.getContextPath()).append("\r\n");
		response.setContentType("text/html");
		PrintWriter outWriter = response.getWriter();
//		HttpSession session = request.getSession();
//		if(session.isNew()){
//			count++;
//		}
		
		outWriter.println("<html><head><title>FirstServlet</title></head><body><p>you have visited this page</p>"+visit+"<p> times</p></body></html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
//	//@Override
//	public void init() throws ServletException{
//		try{
//			BufferedReader br = new BufferedReader(new FileReader("aFile"));
//			count = (new Integer(br.readLine())).intValue();
//		}catch(FileNotFoundException e){
//			throw new UnavailableException("File not found: " + e.toString());
//		}catch(Exception e){
//			throw new UnavailableException("Data problem: " + e.toString());
//		}finally{
//			
//		}
//	}

}
