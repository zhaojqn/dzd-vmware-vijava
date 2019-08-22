/*================================================================================
Copyright (c) 2008 VMware, Inc. All Rights Reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, 
this list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright notice, 
this list of conditions and the following disclaimer in the documentation 
and/or other materials provided with the distribution.

 * Neither the name of VMware, Inc. nor the names of its contributors may be used
to endorse or promote products derived from this software without specific prior 
written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
IN NO EVENT SHALL VMWARE, INC. OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
POSSIBILITY OF SUCH DAMAGE.
================================================================================*/

package com.ustcinfo.sdn.vmware.Service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ustcinfo.sdn.util.JSONUtils;
import com.ustcinfo.sdn.vmware.entity.ResultInfo;
import com.vmware.vim25.ConfigTarget;
import com.vmware.vim25.DatastoreSummary;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.VirtualCdrom;
import com.vmware.vim25.VirtualCdromIsoBackingInfo;
import com.vmware.vim25.VirtualController;
import com.vmware.vim25.VirtualDevice;
import com.vmware.vim25.VirtualDeviceConfigSpec;
import com.vmware.vim25.VirtualDeviceConfigSpecOperation;
import com.vmware.vim25.VirtualIDEController;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.VirtualMachineConfigOption;
import com.vmware.vim25.VirtualMachineConfigSpec;
import com.vmware.vim25.VirtualMachineDatastoreInfo;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.VirtualSCSIController;
import com.vmware.vim25.mo.EnvironmentBrowser;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;
import com.vmware.vim25.mox.VirtualMachineDeviceManager;

/**
 * http://vijava.sf.net
 * 
 * @author Steve Jin
 */
@Service
public class VmDeviceOpService {
	private static final Logger LOGGER = LoggerFactory.getLogger(VmDeviceOpService.class);
	@Autowired
	private ClientSesion clientSesion;

	public String vmCdOper(String vmName, String op) {
		ResultInfo r;
		 String result=""; 
		try {
			 Folder rootFolder = clientSesion.getServiceInstance().getRootFolder();  
				
			 VirtualMachine vm =(VirtualMachine)new InventoryNavigator(rootFolder).searchManagedEntity("VirtualMachine", vmName);  
			 VirtualMachineDeviceManager vmdm= new VirtualMachineDeviceManager(vm);
			 Task task=vmdm.addCdDriveFromIso("[lun8]yz_test_folder/CentOS-7-x86_64-DVD-1511.iso", true);
              if(task.waitForTask().equals(Task.SUCCESS)){  
             	LOGGER.info("{} createVM_Task success ",vmName);
             	r=new ResultInfo("success", "", "");
             }else{  
             	LOGGER.info("{} createVM_Task faile :{}",vmName,task.getTaskInfo().getError().getLocalizedMessage());
             	r=new ResultInfo("faile", "", task.getTaskInfo().getError().getLocalizedMessage());
             	
             }  
		} catch (Exception e) {
			LOGGER.error("vmname operate cd error ",vmName,e);
			r=new ResultInfo("faile", "", "vmname operate cd error");
		}
		try {
			result= JSONUtils.obj2json(r);
		} catch (JsonProcessingException e) {
			LOGGER.error("obj2json error ",e);
		}
		return result;
	}

	 



	 
}
