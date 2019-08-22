package com.ustcinfo.sdn.vmware.controller;

import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ustcinfo.sdn.vmware.Service.ClientSesion;
import com.ustcinfo.sdn.vmware.Service.ClusterService;
import com.ustcinfo.sdn.vmware.Service.DataStoreService;
import com.ustcinfo.sdn.vmware.Service.VmOperaService;
import com.ustcinfo.sdn.vmware.Service.VmService;
import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.RuntimeFault;


@Controller
public class DataStoreController {
	private static final Logger LOGGER=LoggerFactory.getLogger(DataStoreController.class);

	@Autowired
	private DataStoreService d;
 
 
	@RequestMapping(value="/getDataStoreDetail",produces="application/json;charset=UTF-8")
	@ResponseBody
	public  String getDataStoreDetail(String type ,String name) {
		
		return d.getDataStoreDetail(type,name);
	}
  
	
}
