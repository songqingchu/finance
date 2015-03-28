package com.taobao.finance.anasys.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.taobao.finance.common.Store;
import com.taobao.finance.entity.GUser;
import com.taobao.finance.service.GUserService;


@Controller
public class HomeController {

	private static final Logger logger = Logger.getLogger("fileLogger");
	@Autowired
	private Store store;
	
	@Autowired
	private GUserService gUserService;
	
	
	
	
	@RequestMapping(value = "/bb.json", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> home2() throws IOException, ParseException {
		logger.info("requesting home");
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> m=MockUtil.mockData2();
		map.put("series", m.get("date"));
		map.put("sRate", m.get("sRate"));
		map.put("r", m.get("r"));
		
		return map;
	}


	
	@RequestMapping(value = "/gotoRegister.do", method = RequestMethod.GET)
	public String gotoRegister() {
		return "register";
	}

	


	
	@RequestMapping(value = "/chart.do", method = RequestMethod.GET)
	public String chart(HttpServletRequest request) {
		return "c";
	}
	
	@RequestMapping(value = "/security.do", method = RequestMethod.GET)
	public String gotoLogin(HttpServletRequest request) {
		return "login";
	}
	
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public String login(HttpServletRequest request,HttpServletResponse response,
			@RequestParam String userName,@RequestParam String passWord) throws IOException {
		boolean success=false;
		if(StringUtils.isNotBlank(userName)&&StringUtils.isNotBlank(passWord)){
			GUser user=this.gUserService.queryByName(userName);
			if(user!=null){
				if(user.getPassword().equals(passWord)){
					success=true;
					int seconds=30*24*60*60;  
	                Cookie cookie = new Cookie("user", user.getUserName()+"=="+user.getPassword());  
	                cookie.setMaxAge(seconds);                     
	                response.addCookie(cookie);  
				}
			}
			if(success){
				request.getSession().setMaxInactiveInterval(60*60);
				request.getSession().setAttribute("login", true);
				if(userName.contains("root")){
					request.getSession().setAttribute("root", true);
				}
			    request.getSession().setAttribute("user", user);
			    
			}
		}
		if(success){
			  response.sendRedirect(request.getContextPath() + "/record.do");  
			  return null;
		}else{
			return "login";
		}
	}
	
	@RequestMapping(value = "/register.do", method = RequestMethod.POST)
	public String register(HttpServletRequest request,HttpServletResponse response,
			@RequestParam String userName,@RequestParam String passWord) throws IOException {
		boolean success=false;
		String message="";
		if(StringUtils.isNotBlank(userName)&&StringUtils.isNotBlank(passWord)){
			GUser user=this.gUserService.queryByName(userName);
			if(user!=null){
				success=false;
				message="username already exist!";
				request.setAttribute("message", message);
			}else{
				user=new GUser();
				user.setUserName(userName);
				user.setPassword(passWord);
				this.gUserService.insert(user);
				

				int seconds=30*24*60*60;  
		        Cookie cookie = new Cookie("user", user.getUserName()+"=="+user.getPassword());  
		        cookie.setMaxAge(seconds);                     
		        response.addCookie(cookie);  

				request.getSession().setMaxInactiveInterval(60*60);
				request.getSession().setAttribute("login", true);
				request.getSession().setAttribute("user", user);
				
				success=true;
			}
		}
		if(success){  
			return "publicPool";
		}else{
			return "register";
		}
	}
	
	
	@RequestMapping(value = "/loginOut.do", method = RequestMethod.GET)
	public String loginOut(HttpServletRequest request) {
		request.getSession().removeAttribute("user");;
		request.getSession().removeAttribute("login");
		request.getSession().removeAttribute("root");
		return "login";
	}
}
