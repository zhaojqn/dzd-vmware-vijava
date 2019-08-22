package com.dzd.sdn.vmware.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dzd.sdn.vmware.Service.HostService;



@Controller
public class HostController {
	private static final Logger LOGGER=LoggerFactory.getLogger(HostController.class);

	@Autowired
	private HostService hostService;
	@RequestMapping(value="/getHostDetial",produces="application/json;charset=UTF-8")
	@ResponseBody
	public  String getHostDetial(String hostName){
		LOGGER.info("hostName={}",hostName);
		return hostService.getHostdetailByName(hostName);
	}
}
