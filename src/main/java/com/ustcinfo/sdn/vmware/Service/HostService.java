package com.ustcinfo.sdn.vmware.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ustcinfo.sdn.util.JSONUtils;
import com.vmware.vim25.HostCapability;
import com.vmware.vim25.HostConfigInfo;
import com.vmware.vim25.HostListSummary;

import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;

@Service
public class HostService {
	private static final Logger LOGGER=LoggerFactory.getLogger(HostService.class);
	@Autowired
	private ClientSesion clientSesion;
	//192.168.80.22
	public String  getHostdetailByName(String hostName ) {
		  String result="";
		 try {
			 Folder rootFolder = clientSesion.getServiceInstance().getRootFolder();  
				
	         ManagedEntity[] mes =new InventoryNavigator(rootFolder).searchManagedEntities("HostSystem");  
	       
	         LOGGER.info("VirtualMachine numbers:" + mes.length);  

	         //遍历Vcenter 下的服务器资源  
	            for(int i=0;i<mes.length;i++){  
	                HostSystem systems=(HostSystem)mes[i];  
	                /*//服务器配置信息  
	                HostConfigInfo hostConfigInfo=systems.getConfig();  
	                //服务器容量信息  
	                HostCapability hostCapability=systems.getCapability();  */
	                //vm 最多运行数量  
	                HostListSummary  hostListSummary=systems.getSummary();
	                if(hostListSummary.getConfig().getName().equals(hostName)){
	                	 result=JSONUtils.obj2json(hostListSummary );
	                }
	  
	            }  
			 
		} catch (Exception e) {
			LOGGER.error("getVmdetail error:",e);
		}
       return result ;
	}
	
	
	public String  getHostdetail() {
		  String result="";
		 try {
			 Folder rootFolder = clientSesion.getServiceInstance().getRootFolder();  
				
	         ManagedEntity[] mes =new InventoryNavigator(rootFolder).searchManagedEntities("HostSystem");  
	       
	         LOGGER.info("VirtualMachine numbers:" + mes.length);  

	         //遍历Vcenter 下的服务器资源  
	            for(int i=0;i<mes.length;i++){  
	                HostSystem systems=(HostSystem)mes[i];  
	                //服务器配置信息  
	                HostConfigInfo hostConfigInfo=systems.getConfig();  
	                //服务器容量信息  
	                HostCapability hostCapability=systems.getCapability();  
	                //vm 最多运行数量  
 	                HostListSummary  hostListSummary=systems.getSummary();
	  
	            }  
			 
		} catch (Exception e) {
			LOGGER.error("getVmdetail error:",e);
		}
         return result ;
	}
}
