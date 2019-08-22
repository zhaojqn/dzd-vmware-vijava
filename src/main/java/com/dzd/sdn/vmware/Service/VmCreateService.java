package com.dzd.sdn.vmware.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dzd.sdn.util.CommUtil;
import com.vmware.vim25.Description;
import com.vmware.vim25.VirtualDeviceConfigSpec;
import com.vmware.vim25.VirtualDeviceConfigSpecFileOperation;
import com.vmware.vim25.VirtualDeviceConfigSpecOperation;
import com.vmware.vim25.VirtualDisk;
import com.vmware.vim25.VirtualDiskFlatVer2BackingInfo;
import com.vmware.vim25.VirtualEthernetCard;
import com.vmware.vim25.VirtualEthernetCardNetworkBackingInfo;
import com.vmware.vim25.VirtualLsiLogicController;
import com.vmware.vim25.VirtualMachineConfigSpec;
import com.vmware.vim25.VirtualMachineFileInfo;
import com.vmware.vim25.VirtualPCNet32;
import com.vmware.vim25.VirtualSCSISharing;
import com.vmware.vim25.mo.Datacenter;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ResourcePool;
import com.vmware.vim25.mo.Task;

@Service
public class VmCreateService {
	private static final Logger LOGGER = LoggerFactory.getLogger(VmCreateService.class);
	@Autowired
	private ClientSesion clientSesion;
	 
 	
 	@SuppressWarnings("deprecation")
	public void createVm (String dcName,String poolName,String hostName,String datastoreName,String vmName  ){
 		try{  
            
             ResourcePool pool = null;  
             HostSystem hostSystem = null;  
             InventoryNavigator inventoryNavigator = null;  
            Task task = null;  
            Datacenter dc=null;
            Folder vmFolder=null;
            Folder rootFolder = clientSesion.getServiceInstance().getRootFolder();
            inventoryNavigator = new InventoryNavigator(rootFolder);  
           
            try {  
            	  dc = (Datacenter) new InventoryNavigator(
                         rootFolder).searchManagedEntity("Datacenter", dcName);
                  vmFolder = dc.getVmFolder();  
            } catch (Exception e) {  
            	LOGGER.error("{} get  error :",dcName, e);  
  
            }  
            
            
             if (null != poolName && !"".equals(poolName)) {  
                try {  
                    pool = (ResourcePool) inventoryNavigator.searchManagedEntity("ResourcePool", poolName);  
                       
                } catch (Exception e) {  
                	LOGGER.error("{} get  error :",poolName, e);
                }  
  
            } else {  
  
                try {  
                	hostSystem = (HostSystem) inventoryNavigator.searchManagedEntity("HostSystem", hostName);  
  
                  
  
                } catch (Exception e) {  
                	LOGGER.error("{} get  error :",hostName, e);
                }  
  
            }  
  
            VirtualMachineConfigSpec configSpec = new VirtualMachineConfigSpec();  
            configSpec.setName(vmName);//
            configSpec.setGuestId("centos64Guest");
            configSpec.setNumCPUs(Integer.parseInt("1"));  
            configSpec.setMemoryMB(Long.parseLong("1024"));  
  
           
           /* int cKey = 1000;
            VirtualDeviceConfigSpec virtualDeviceConfigSpec =  createDiskSpec(  
            		datastoreName,cKey,  
                    40*1024*1024, "persistent",vmName);  
            if (virtualDeviceConfigSpec != null) {  
               System.out.println("创建disk不为空");  
            } else {  
                System.out.println("创建disk为空");  
            }  
  
            configSpec.setDeviceChange(new VirtualDeviceConfigSpec[] { virtualDeviceConfigSpec });  */
         // create virtual devices
            int cKey = 1000;
            VirtualDeviceConfigSpec scsiSpec = createScsiSpec(cKey);
            VirtualDeviceConfigSpec diskSpec = createDiskSpec(
                datastoreName, cKey, 40*1024*1024, "persistent",vmName);
            /*VirtualDeviceConfigSpec nicSpec = createNicSpec(
                netName, nicName);*/

            configSpec.setDeviceChange(new VirtualDeviceConfigSpec[] 
                {scsiSpec, diskSpec});
            
            // create vm file info for the vmx file
            VirtualMachineFileInfo vmfi = new VirtualMachineFileInfo();
            vmfi.setVmPathName("["+ datastoreName +"]");
            configSpec.setFiles(vmfi);
            
            try {  
            	LOGGER.info("{}   Task start ",vmName);
            	   task = vmFolder.createVM_Task(configSpec, pool, null);
                String result=task.waitForTask();  
                if(result.equals(Task.SUCCESS)){  
                	LOGGER.info("{} createVM_Task success ",vmName);
                }else{  
                	LOGGER.info("{} createVM_Task faile :{}",vmName,task.getTaskInfo().getError().getLocalizedMessage());
                	
                }  
  
  
            } catch (Exception e) {  
            	LOGGER.error("{}  create error :",vmName, e);
  
            }  
        }catch (Exception e){  
        	LOGGER.error("{} create  error :",vmName, e);
        }  
 	}

 	
 	
 	
 	 public  VirtualDeviceConfigSpec createDiskSpec(String dsName, int cKey, long diskSizeKB, String diskMode,String vmName)
 		  {
 		    VirtualDeviceConfigSpec diskSpec = new VirtualDeviceConfigSpec();
 		    diskSpec.setOperation(VirtualDeviceConfigSpecOperation.add);
 		    diskSpec.setFileOperation(VirtualDeviceConfigSpecFileOperation.create);
 		    VirtualDisk vd = new VirtualDisk();
 		    vd.setCapacityInKB(diskSizeKB);
 		    diskSpec.setDevice(vd);
 		    vd.setKey(0);
 		    vd.setUnitNumber(0);
 		    vd.setControllerKey(cKey);
 		    VirtualDiskFlatVer2BackingInfo diskfileBacking = 
 		        new VirtualDiskFlatVer2BackingInfo();
 			String diskName = vmName+"-"+CommUtil.getStringRandom(8);  
			String fileName = "[" + dsName + "] " + vmName + "/" + diskName  
			+ ".vmdk"; 
 		    diskfileBacking.setFileName(fileName);
 		    diskfileBacking.setDiskMode(diskMode);
 		    diskfileBacking.setThinProvisioned(true);
 		    vd.setBacking(diskfileBacking);
 		    return diskSpec;
 		  }
 
 	
 	public  VirtualDeviceConfigSpec createScsiSpec(int cKey)
	  {
	    VirtualDeviceConfigSpec scsiSpec = 
	      new VirtualDeviceConfigSpec();
	    scsiSpec.setOperation(VirtualDeviceConfigSpecOperation.add);
	    VirtualLsiLogicController scsiCtrl = 
	        new VirtualLsiLogicController();
	    scsiCtrl.setKey(cKey);
	    scsiCtrl.setBusNumber(0);
	    scsiCtrl.setSharedBus(VirtualSCSISharing.noSharing);
	    scsiSpec.setDevice(scsiCtrl);
	    return scsiSpec;
	  }
 	public  VirtualDeviceConfigSpec createNicSpec(String netName, 
 	      String nicName) throws Exception
 	  {
 	    VirtualDeviceConfigSpec nicSpec = 
 	        new VirtualDeviceConfigSpec();
 	    nicSpec.setOperation(VirtualDeviceConfigSpecOperation.add);

 	    VirtualEthernetCard nic =  new VirtualPCNet32();
 	    VirtualEthernetCardNetworkBackingInfo nicBacking = 
 	        new VirtualEthernetCardNetworkBackingInfo();
 	    nicBacking.setDeviceName(netName);

 	    Description info = new Description();
 	    info.setLabel(nicName);
 	    info.setSummary(netName);
 	    nic.setDeviceInfo(info);
 	    
 	    // type: "generated", "manual", "assigned" by VC
 	    nic.setAddressType("generated");
 	    nic.setBacking(nicBacking);
 	    nic.setKey(0);
 	   
 	    nicSpec.setDevice(nic);
 	    return nicSpec;
 	  }
 	
	 
}
