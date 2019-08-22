package com.dzd.sdn.vmware.Service;

import java.util.ArrayList;

import com.dzd.sdn.vmware.entity.DataCenterInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dzd.sdn.util.JSONUtils;
import com.vmware.vim25.mo.Datacenter;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;

@Service
public class DataCenterService {
	private static final Logger LOGGER=LoggerFactory.getLogger(DataCenterService.class);
	@Autowired
	private ClientSesion clientSesion;
	//192.168.80.22

	
	public String  getDataCenterDetail() {
		  String result="";
		  ArrayList<DataCenterInfo> list = new ArrayList<DataCenterInfo>();
		 try {
			 Folder rootFolder = clientSesion.getServiceInstance().getRootFolder();  
				
			// ManagedEntity[] dcs = new InventoryNavigator(rootFolder).searchManagedEntities(new String[][] { {"Datacenter", "name" }, }, true); 
			 ManagedEntity[] dcs = new InventoryNavigator(rootFolder).searchManagedEntities("Datacenter" );  
	            for(int i=0;i<dcs.length;i++)  
	            {  
	                try  
	                {  
	                    Datacenter dataCenter = (Datacenter)dcs[i];  
	                    DataCenterInfo center = new DataCenterInfo();  
	                    center.setName(dataCenter.getName());  
	                    center.setVmFolterName(dataCenter.getVmFolder().getName());  
	                    center.setNetworkFolder(dataCenter.getNetworkFolder().getName());  
	                    center.setDatastoreFolderName(dataCenter.getDatastoreFolder().getName());  
	                    center.setHostFolderName(dataCenter.getDatastoreFolder().getName());  
	                    center.setProgId(dataCenter.getMOR().val);  
	                    list.add(center); 
	                }catch(Exception e){  
	                   LOGGER.error("得取数据中心失败:"+e.getMessage());  
	                } 
	                result=JSONUtils.obj2json(list) ;
	            }  
			 
		} catch (Exception e) {
			LOGGER.error("getDataCenterDetail error:",e);
		}
         return result ;
	}
}
