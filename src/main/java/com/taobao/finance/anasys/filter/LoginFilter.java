package com.taobao.finance.anasys.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter extends HttpServlet implements Filter {  
    private static final long serialVersionUID = 1L;  
  
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain) throws IOException, ServletException {  
           HttpServletRequest request=(HttpServletRequest)arg0;     
           HttpServletResponse response  =(HttpServletResponse) arg1;      
           HttpSession session = request.getSession(true);       
           String url=request.getRequestURI();    
           Object login=session.getAttribute("login");
           Boolean loginSuccess=false;
           if(login!=null){
               if((Boolean)login.equals(true)){
            	   loginSuccess=true;
        	   } 
           }
           if(loginSuccess || ( url.indexOf("Login")>0 || url.indexOf("login")>0 )|| url.indexOf("security")>0){
        	   chain.doFilter(arg0, arg1);  
           }else{
        	   response.sendRedirect(request.getContextPath() + "/security.do");   
           }
           
           chain.doFilter(arg0, arg1);
           
    }  
    public void init(FilterConfig arg0) throws ServletException {  
    }  
  
}  
