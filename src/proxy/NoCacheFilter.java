package proxy;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoCacheFilter implements Filter{

  private FilterConfig filterConfig=null;

  public void init(FilterConfig filterConfig){
    this.filterConfig=filterConfig;
  }

  public void destroy(){
    this.filterConfig=null;
  }

  public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain) throws IOException,ServletException{
//      HttpServletResponse httpResponse=(HttpServletResponse)response;
      HttpServletRequest httpRequest = (HttpServletRequest) request;
      
//      httpResponse.setHeader("Cache-Control","no-cache");
//      httpResponse.setDateHeader("Expires",0);
//      httpResponse.setHeader("Pragma","No-cache");
      chain.doFilter(request,response);
  }
}