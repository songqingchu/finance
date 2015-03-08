package com.taobao.finance.anasys.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.taobao.finance.entity.GUser;
import com.taobao.finance.service.GUserService;

@Controller
public class LoginFilter extends HttpServlet implements Filter {  
    private static final long serialVersionUID = 1L;  
    public static Set<String> whiteList=new HashSet<String>();
    
    @Autowired
	private GUserService gUserService;
    
    
    
    static{
    	whiteList.add("Login");
    	whiteList.add("Login");
    	whiteList.add("security");
    	whiteList.add("publicPool");
    	whiteList.add("kData");
    	whiteList.add("register");
    }
  
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain) throws IOException, ServletException {  
           HttpServletRequest request=(HttpServletRequest)arg0;     
           HttpServletResponse response  =(HttpServletResponse) arg1;      
           HttpSession session = request.getSession(true);       
          // rememberMe(request);
           String url=request.getRequestURI();    
           Object login=session.getAttribute("login");
           Boolean loginSuccess=false;
           if(login!=null){
               if((Boolean)login.equals(true)){
            	   loginSuccess=true;
        	   } 
           }
           if(loginSuccess || isInWhiteList(url)){
        	   chain.doFilter(arg0, arg1);  
           }else{
        	   response.sendRedirect(request.getContextPath() + "/security.do");   
           }
    }  
    
    public void rememberMe(HttpServletRequest request){
    	Cookie[] cookies = request.getCookies();  
        String[] cooks = null;  
        String username = null;  
        String password = null;  
        if (cookies != null) {  
            for (Cookie coo : cookies) {  
                String aa = coo.getValue();
                if(coo.getName().equals("user")){
                	cooks = aa.split("==");  
                    if (cooks.length == 2) {  
                        username = cooks[0];  
                        password = cooks[1];  
                    	GUser user=this.gUserService.queryByName(username);
            			if(user!=null){
            				if(user.getPassword().equals(password)){
            					request.getSession().setAttribute("login", true);
            					request.getSession().setAttribute("user", user);
            				}
            			}
                    }  	
                }
            }  
        }  
    }
    
    public boolean isInWhiteList(String uri){
        for(String s:whiteList){
        	if(uri.toUpperCase().contains(s.toUpperCase())){
        		return true;
        	}
        }
    	return false;
    }
    public void init(FilterConfig arg0) throws ServletException {  
    }  
  
}  
