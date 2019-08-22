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
import com.ustcinfo.sdn.vmware.Service.VmOperaService;
import com.ustcinfo.sdn.vmware.Service.VmService;
import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.RuntimeFault;


@Controller
public class ClusterController {
	private static final Logger LOGGER=LoggerFactory.getLogger(ClusterController.class);

	@Autowired
	private ClusterService c;
 
	@RequestMapping(value="/getClusterDetailByDcName",produces="application/json;charset=UTF-8")
	@ResponseBody
	public  String getClusterDetailByDcName(String dcName) {
		
		return c.getClusterDetailByDcName(dcName);
	}
	@RequestMapping(value="/getClusterDetail",produces="application/json;charset=UTF-8")
	@ResponseBody
	public  String getClusterDetail() {
		
		return c.getClusterDetail();
	}
  
	
}
