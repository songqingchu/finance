package com.taobao.finance.anasys.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.finance.anasys.model.MyEntity;

/**
 * Handles requests for the application home page.
 * 
 * hello
 */
@Controller
public class EntityController {

	private static final Logger logger = LoggerFactory.getLogger(EntityController.class);

	private String a;
	
	@ModelAttribute("entity")
	public MyEntity init(@PathVariable String id) {
		return null;
		
	}
	
	@RequestMapping(value="/myentity/{id}", method=RequestMethod.GET)
	public ModelAndView view(ModelAndView mv, @ModelAttribute("entity") MyEntity entity) {
		mv.addObject("entity", entity);
		mv.setViewName("entity_detail");
		logger.info("requesting /myentity");
		return mv;
	}

	@RequestMapping(value="/myentity/{id}", method=RequestMethod.POST)
	public String update(ModelAndView mv, @ModelAttribute("entity") MyEntity entity) {
	    	logger.info("updating /myentity");
		return "redirect:/myentity/"+1;
	}
}

