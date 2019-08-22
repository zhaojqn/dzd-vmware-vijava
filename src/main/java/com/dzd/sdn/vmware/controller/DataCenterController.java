package com.dzd.sdn.vmware.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dzd.sdn.vmware.Service.DataCenterService;


@Controller
public class DataCenterController {
	private static final Logger LOGGER=LoggerFactory.getLogger(DataCenterController.class);

	@Autowired
	private DataCenterService d;
	 
	@RequestMapping(value="/getDataCenterDetail",produces="application/json;charset=UTF-8")
	@ResponseBody
	public  String getDataCenterDetail() {
		
		return d.getDataCenterDetail();
	}
 
	
}
