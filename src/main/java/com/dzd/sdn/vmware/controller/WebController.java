package com.dzd.sdn.vmware.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @author dong
 * @date 2017年8月11日 下午4:45:06  
 * @desc
 */
@Controller
public class WebController {
	
	private static final Logger LOGGER=LoggerFactory.getLogger(WebController.class);

	@RequestMapping(value="/index")
	public String index(){
		LOGGER.info("进入index");
		return "/index";
	}
	
	@RequestMapping(value="/whitelist")
	public String whiteList(){
		LOGGER.info("进入index");
		return "whitelist/whitelist";
	}
}
