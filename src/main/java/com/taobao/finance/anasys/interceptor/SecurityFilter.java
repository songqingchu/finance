package com.taobao.finance.anasys.interceptor;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.finance.entity.GUser;
import com.taobao.finance.service.GUserService;

/**
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: www.dianwoba.com</p>
 * @author lijiayang
 * @date   2015骞�3鏈�9鏃�
 */
public class SecurityFilter implements HandlerInterceptor{  
  
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
    
    
    @Override  
    public void afterCompletion(HttpServletRequest request,  
            HttpServletResponse response, Object obj, Exception err)  
            throws Exception {  
    }  
  
    @Override  
    public void postHandle(HttpServletRequest request, HttpServletResponse response,  
            Object obj, ModelAndView mav) throws Exception {  
        
    }  
  
    @Override  
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,  
            Object obj) throws Exception {  
        HttpSession session = request.getSession(true);    
        if(!request.toString().contains("loginOut")){
        	rememberMe(request);
        }
        String url=request.getRequestURI();    
        Object login=session.getAttribute("login");
        Boolean loginSuccess=false;
        if(login!=null){
            if((Boolean)login.equals(true)){
         	   loginSuccess=true;
     	   } 
        }
        if(!(loginSuccess || isInWhiteList(url))){
        	response.sendRedirect(request.getContextPath() + "/security.do");   
        	return false;
        }
        return true;
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
            					if(user.getUserName().contains("root")){
            						request.getSession().setAttribute("root", true);
            					}
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
}  