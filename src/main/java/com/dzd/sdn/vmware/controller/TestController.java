package com.dzd.sdn.vmware.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dzd.sdn.vmware.Service.ClientSesion;


/**
 * @author dong
 * @date 2017年8月11日 下午4:45:06  
 * @desc
 */
@Controller
public class TestController {
	
	private static final Logger LOGGER=LoggerFactory.getLogger(TestController.class);

	@Autowired
	private ClientSesion cs;
	@RequestMapping(value="/connect")
	@ResponseBody
	public String connect(){
		LOGGER.info("connect");
	
		return "";
	}
	
	@RequestMapping(value="/disconnect")
	@ResponseBody
	public String disconnect(){
		LOGGER.info("disconnect");
		cs.disconnect();
		return "sucess";
	}
}
