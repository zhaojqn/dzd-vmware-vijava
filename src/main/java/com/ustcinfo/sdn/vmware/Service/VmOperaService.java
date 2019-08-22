package com.ustcinfo.sdn.vmware.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ustcinfo.sdn.util.CommUtil;
import com.ustcinfo.sdn.util.JSONUtils;
import com.ustcinfo.sdn.vmware.entity.ResultInfo;
import com.vmware.vim25.VirtualDevice;
import com.vmware.vim25.VirtualDeviceConfigSpec;
import com.vmware.vim25.VirtualDeviceConfigSpecFileOperation;
import com.vmware.vim25.VirtualDeviceConfigSpecOperation;
import com.vmware.vim25.VirtualDisk;
import com.vmware.vim25.VirtualDiskFlatVer2BackingInfo;
import com.vmware.vim25.VirtualMachineCloneSpec;
import com.vmware.vim25.VirtualMachineConfigSpec;
import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.VirtualMachineRelocateSpec;
import com.vmware.vim25.mo.ComputeResource;
import com.vmware.vim25.mo.Datastore;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ResourcePool;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;

@Service
public class VmOperaService {
	private static final Logger LOGGER = LoggerFactory.getLogger(VmOperaService.class);
	@Autowired
	private ClientSesion clientSesion;
	// new-安徽电信自动化运维平台01_80.103

	public String vmOperate(String vmName, String op,String newName) {
 		ResultInfo r = null;
 		String result="";
		try {
			Folder rootFolder = clientSesion.getServiceInstance().getRootFolder();

			ManagedEntity mes = new InventoryNavigator(rootFolder).searchManagedEntity("VirtualMachine", vmName);
			if (mes != null) {
				VirtualMachine virtualMachine = (VirtualMachine) mes;
				LOGGER.info("VirtualMachine name:" + virtualMachine.getName());
				 
				if ("reboot".equalsIgnoreCase(op)) {
					//虚拟机重启
					virtualMachine.rebootGuest();
					LOGGER.info(vmName + "  reboot success");
					r=new ResultInfo("success", "", "");
				} else if ("poweron".equalsIgnoreCase(op)) {
					//虚拟机通电
					Task task = virtualMachine.powerOnVM_Task(null);
					if (task.waitForTask() == Task.SUCCESS) {
						LOGGER.info(vmName + " powered on success");
						r=new ResultInfo("success", "", "");
					}else{  
		             	LOGGER.info("{} powered on faile :{}",vmName,task.getTaskInfo().getError().getLocalizedMessage());
		             	r=new ResultInfo("faile", "", task.getTaskInfo().getError().getLocalizedMessage());
		             	
		             }
				} else if ("poweroff".equalsIgnoreCase(op)) {
					//虚拟机断电
					Task task = virtualMachine.powerOffVM_Task();
					if (task.waitForTask() == Task.SUCCESS) {
						LOGGER.info(vmName + " powered off success");
						r=new ResultInfo("success", "", "");
					}else{  
		             	LOGGER.info("{} powered of faile :{}",vmName,task.getTaskInfo().getError().getLocalizedMessage());
		             	r=new ResultInfo("faile", "", task.getTaskInfo().getError().getLocalizedMessage());
		             	
		             }
				} else if ("reset".equalsIgnoreCase(op)) {
					//虚拟机 重置
					Task task = virtualMachine.resetVM_Task();
					if (task.waitForTask() == Task.SUCCESS) {
						LOGGER.info(vmName + " reset success");
						r=new ResultInfo("success", "", "");
					}else{  
		             	LOGGER.info("{} reset faile :{}",vmName,task.getTaskInfo().getError().getLocalizedMessage());
		             	r=new ResultInfo("faile", "", task.getTaskInfo().getError().getLocalizedMessage());
		             	
		             }
				}else if ("reload".equalsIgnoreCase(op)) {
					//虚拟机重新加载 
					virtualMachine.reload();
					LOGGER.info(vmName + "   reload success");
					r=new ResultInfo("success", "", "");
				} else if ("standby".equalsIgnoreCase(op)) {
					 //虚拟机待机
					virtualMachine.standbyGuest();
					LOGGER.info(vmName + "  stoodby success");
					r=new ResultInfo("success", "", "");
				} else if ("suspend".equalsIgnoreCase(op)) {
					Task task = virtualMachine.suspendVM_Task();
					if (task.waitForTask() == Task.SUCCESS) {
						LOGGER.info(vmName + " suspended success");
						r=new ResultInfo("success", "", "");
					}else{  
		             	LOGGER.info("{} suspended faile :{}",vmName,task.getTaskInfo().getError().getLocalizedMessage());
		             	r=new ResultInfo("faile", "", task.getTaskInfo().getError().getLocalizedMessage());
		             	
		             }
				} else if ("shutdown".equalsIgnoreCase(op)) {
					//虚拟机关机  
	                virtualMachine.shutdownGuest(); 
	                LOGGER.info(vmName + " guest OS shutdown");
	                r=new ResultInfo("success", "", "");
				}else if ("rename".equalsIgnoreCase(op)) {
					//虚拟机重新 命名 
	                virtualMachine.rename_Task(newName); 
	                LOGGER.info(vmName + " guest OS shutdown");
	                r=new ResultInfo("success", "", "");
				}else if("delete".equalsIgnoreCase(op)){
					if(!virtualMachine.getRuntime().getPowerState().name().equalsIgnoreCase(VirtualMachinePowerState.poweredOff.name()))//判断虚拟机是否断电
						 LOGGER.info(vmName + " poweredOff");
					Task task = virtualMachine.destroy_Task();//执行指定虚拟机的销毁工作。
 					if (task.waitForTask() == Task.SUCCESS) {
						LOGGER.info(vmName + " delete");
						r=new ResultInfo("success", "", "");
					}else{  
		             	LOGGER.info("{} createVM_Task faile :{}",vmName,task.getTaskInfo().getError().getLocalizedMessage());
		             	r=new ResultInfo("faile", "", task.getTaskInfo().getError().getLocalizedMessage());
		             	
		             }
				}
				
			}

		} catch (Exception e) {
			LOGGER.error("{} {}  error :",vmName,op, e);
			r=new ResultInfo("faile", "", vmName +" "+op+" error");
		}
		try {
			result= JSONUtils.obj2json(r);
		} catch (JsonProcessingException e) {
			LOGGER.error("obj2json error ",e);
		}
		return result;

	}
 	
 	
 	public void createVmByTemp(String templateVMName,String poolName,String hostName,String datastoreName,String vmName  ){
 		try{  
            
            VirtualMachine templateVM = null;  
            ResourcePool pool = null;  
            ComputeResource computerResource = null;  
            Datastore datastore = null;  
            InventoryNavigator inventoryNavigator = null;  
            Task task = null;  
  
            Folder rootFolder = clientSesion.getServiceInstance().getRootFolder();
            inventoryNavigator = new InventoryNavigator(rootFolder);  
            try {  
                templateVM = ( VirtualMachine) inventoryNavigator.searchManagedEntity(  
                        "VirtualMachine", templateVMName);  
            } catch (Exception e) {  
            	LOGGER.error("{} get  error :",templateVMName, e);
            }  
            
            try {  
                datastore = (Datastore) inventoryNavigator.searchManagedEntity("Datastore",  
                                datastoreName);  
            } catch (Exception e) {  
            	LOGGER.error("{} get  error :",datastoreName, e);  
  
            }  
            
            
            VirtualMachineRelocateSpec virtualMachineRelocateSpec = new VirtualMachineRelocateSpec();  
            if (null != poolName && !"".equals(poolName)) {  
                try {  
                    pool = (ResourcePool) inventoryNavigator.searchManagedEntity("ResourcePool", poolName);  
                    virtualMachineRelocateSpec.setPool(pool.getMOR());  
                    virtualMachineRelocateSpec.setDatastore(datastore  
                            .getMOR());  
                } catch (Exception e) {  
                	LOGGER.error("{} get  error :",poolName, e);
                }  
  
            } else {  
  
                try {  
                    computerResource = (ComputeResource) inventoryNavigator.searchManagedEntity("ComputeResource", hostName);  
  
                    virtualMachineRelocateSpec.setPool(computerResource.getResourcePool().getMOR());  
                    virtualMachineRelocateSpec.setHost(computerResource.getHosts()[0].getMOR());  
                    virtualMachineRelocateSpec.setDatastore(datastore  
                            .getMOR());  
  
                } catch (Exception e) {  
                	LOGGER.error("{} get  error :",hostName, e);
                }  
  
            }  
  
            VirtualMachineConfigSpec configSpec = new VirtualMachineConfigSpec();  
  
            configSpec.setNumCPUs(Integer.parseInt("1"));  
            configSpec.setMemoryMB(Long.parseLong("1024"));  
  
            VirtualMachineCloneSpec cloneSpec =  
                    new VirtualMachineCloneSpec();  
            VirtualDeviceConfigSpec virtualDeviceConfigSpec =  createDiskSpec(  
            		datastoreName,  
                    50*1024*1024, "persistent",templateVM);  
            if (virtualDeviceConfigSpec != null) {  
               System.out.println("创建disk不为空");  
            } else {  
                System.out.println("创建disk为空");  
            }  
  
            configSpec 
                    .setDeviceChange(new VirtualDeviceConfigSpec[] { virtualDeviceConfigSpec });  
            cloneSpec.setLocation(virtualMachineRelocateSpec);  
            cloneSpec.setPowerOn(true);  
            cloneSpec.setTemplate(false);  
            cloneSpec.setConfig(configSpec);  

            try {  
            	LOGGER.info("{} clone {} Task start ",templateVMName,vmName);
                task = templateVM.cloneVM_Task((Folder) templateVM.getParent(),  
                		vmName, cloneSpec);  
                String result=task.waitForTask();  
                if(result.equals(Task.SUCCESS)){  
                	LOGGER.info("{} clone {} Task success ",templateVMName,vmName);
                }else{  
                	LOGGER.info("{} clone {} Task faile ",templateVMName,vmName);
                }  
  
  
            } catch (Exception e) {  
            	LOGGER.error("{}  create error :",vmName, e);
  
            }  
        }catch (Exception e){  
        	LOGGER.error("{} create  error :",vmName, e);
        }  
 	}
 	 public static VirtualDeviceConfigSpec createDiskSpec(String dsName,  
             long diskSizeKB, String diskMode, VirtualMachine vm) {  
 		 
			VirtualDeviceConfigSpec diskSpec = new VirtualDeviceConfigSpec();  
			diskSpec.setOperation(VirtualDeviceConfigSpecOperation.add);  
			diskSpec.setFileOperation(VirtualDeviceConfigSpecFileOperation.create);  
			VirtualDisk vd = new VirtualDisk();  
			vd.setCapacityInKB(diskSizeKB);  
			diskSpec.setDevice(vd);  
			vd.setKey(getkey(vm));  
			vd.setUnitNumber(getUnitNumber(vm));  
			vd.setControllerKey(getcontrollerkey(vm));  
			VirtualDiskFlatVer2BackingInfo diskfileBacking = new VirtualDiskFlatVer2BackingInfo();  
			String diskName = vm.getName()+"-"+CommUtil.getStringRandom(8);  
			String fileName = "[" + dsName + "] " + vm.getName() + "/" + diskName  
			+ ".vmdk";  
			diskfileBacking.setFileName(fileName);  
			diskfileBacking.setDiskMode(diskMode);  
			diskfileBacking.setThinProvisioned(true);  
			vd.setBacking(diskfileBacking);  
			return diskSpec;  
		}  
 	
 	
 // 获取虚拟机磁盘管理的controlerkey  
    public static int getcontrollerkey(VirtualMachine vm) {  
        int controllerkey = 0;  
        if (vm != null) {  
            VirtualDevice[] devices = vm.getConfig().getHardware().getDevice();  
            if (devices != null && devices.length > 0) {  
                for (int i = 0; i < devices.length; i++) {  
                    VirtualDevice device = devices[i];  
                    if (device instanceof VirtualDisk) {  
                        controllerkey = device.getControllerKey();  
                    }  
                }  
            }  
        }  
        return controllerkey;  
    }  
  
    // 获取虚拟机已生成key  
    public static int getkey(VirtualMachine vm) {  
        int key = 0;  
        if (vm != null) {  
            VirtualDevice[] devices = vm.getConfig().getHardware().getDevice();  
            if (devices != null && devices.length > 0) {  
                for (int i = 0; i < devices.length; i++) {  
                    VirtualDevice device = devices[i];  
                    if (device instanceof VirtualDisk) {  
                        key = device.getKey();  
                    }  
                }  
            }  
        }  
        key = key + 1;  
        return key;  
    }  
  
    // 获取虚拟机已生成UnitNumber  
    public static int getUnitNumber(VirtualMachine vm) {  
        int UnitNumber = 0;  
        if (vm != null) {  
            VirtualDevice[] devices = vm.getConfig().getHardware().getDevice();  
            if (devices != null && devices.length > 0) {  
                for (int i = 0; i < devices.length; i++) {  
                    VirtualDevice device = devices[i];  
                    if (device instanceof VirtualDisk) {  
                        UnitNumber = device.getUnitNumber();  
                    }  
                }  
            }  
        }  
        UnitNumber = UnitNumber + 1;  
        return UnitNumber;  
    }  
}
