package com.ustcinfo.sdn.vmware.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ustcinfo.sdn.vmware.Service.VmCreateService;
import com.ustcinfo.sdn.vmware.Service.VmDeviceOpService;
import com.ustcinfo.sdn.vmware.Service.VmOperaService;
import com.ustcinfo.sdn.vmware.Service.VmService;

@Controller
public class VmController {
	private static final Logger LOGGER=LoggerFactory.getLogger(VmController.class);

	@Autowired
	private VmService v;
	@Autowired
	private VmOperaService vmOper;
	@Autowired
	private VmCreateService vmCreate;
	@Autowired
	private VmDeviceOpService vmDeop;
	
	
	
	
	@RequestMapping(value="/getVmDetial",produces="application/json;charset=UTF-8")
	@ResponseBody
	public  String getVmDetial() {
		
		return v.getVmdetail();
	}
	
 
	@RequestMapping(value="/getVMByName",produces="application/json;charset=UTF-8")
	@ResponseBody
	public  String getVMByName(String vmName) {
		
		return v.getVMByName(vmName);
	}
	
	@RequestMapping(value="/getVMCPUPerByTimeRange",produces="application/json;charset=UTF-8")
	@ResponseBody
	public  String getVMCPUPerByTimeRange(String vmName) {
		
		 v.getVMCPUPerByTimeRange(vmName,20, ""); 
		 return "{\"result\":\"request success\"}";
	}
	@RequestMapping(value="/createVmByTemp",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String createVmByTemp(String templateVMName,String poolName,String hostName,String datastoreName,String vmName  ){
		vmOper.createVmByTemp(templateVMName, poolName, hostName, datastoreName,vmName);
		return "{\"result\":\"request success\"}";
	}
	/*op类型:reboot  poweron poweroff reset reload standby待机  suspend挂起 shutdown   rename*/ 
	@RequestMapping(value="/vmOperate",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String vmOperate(String vmName, String op,String newName  ){
		String result=vmOper.vmOperate(vmName, op, newName);
		return result;
	}
	@RequestMapping(value="/createVm",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String createVm (String dcName,String poolName,String hostName,String datastoreName,String vmName  ){
		vmCreate.createVm(dcName, poolName, hostName, datastoreName,vmName);
		return "{\"result\":\"request success\"}";
	}
	@RequestMapping(value="/vmCDOper",produces="application/json;charset=UTF-8")
	@ResponseBody
	public String vmCDOper(String vmName, String op) {
		LOGGER.info("{};{}",vmName,op);
		return vmDeop.vmCdOper(vmName, op);
	}
}
