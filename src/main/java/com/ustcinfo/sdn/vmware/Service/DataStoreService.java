package com.ustcinfo.sdn.vmware.Service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ustcinfo.sdn.util.JSONUtils;
import com.ustcinfo.sdn.vmware.entity.ClusterInfo;
import com.ustcinfo.sdn.vmware.entity.DataCenterInfo;
import com.ustcinfo.sdn.vmware.entity.HostDatastoreInfo;
import com.ustcinfo.sdn.vmware.entity.HostDatastoreMountInfo;
import com.ustcinfo.sdn.vmware.entity.VirtualVmFile;
import com.vmware.vim25.ArrayOfHostDatastoreBrowserSearchResults;
import com.vmware.vim25.ClusterFailoverResourcesAdmissionControlPolicy;
import com.vmware.vim25.DatastoreSummary;
import com.vmware.vim25.FileInfo;
import com.vmware.vim25.FileQuery;
import com.vmware.vim25.HostCapability;
import com.vmware.vim25.HostConfigInfo;
import com.vmware.vim25.HostDatastoreBrowserSearchResults;
import com.vmware.vim25.HostDatastoreBrowserSearchSpec;
import com.vmware.vim25.HostMountInfo;
import com.vmware.vim25.TaskInfo;
import com.vmware.vim25.VirtualDeviceConfigSpecOperation;
import com.vmware.vim25.VmDiskFileQuery;
import com.vmware.vim25.VmDiskFileQueryFilter;
import com.vmware.vim25.mo.ClusterComputeResource;
import com.vmware.vim25.mo.Datacenter;
import com.vmware.vim25.mo.Datastore;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostDatastoreBrowser;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.Task;

@Service
public class DataStoreService {
	private static final Logger LOGGER=LoggerFactory.getLogger(DataStoreService.class);
	@Autowired
	private ClientSesion clientSesion;
	//192.168.80.22
	/**  
	 * 数据集群发现  
	 * @param serviceInstance  
	 * @return  
	 */  
	public String  getDataStoreDetail(String type,String name) {
		  String result="";
		  ArrayList<HostDatastoreInfo> list = new ArrayList<HostDatastoreInfo>(); 
		  Datastore[] datastores= null;
		 try {
			 Folder rootFolder = clientSesion.getServiceInstance().getRootFolder();  
			
			 if("HostSystem".equals(type)){
				 HostSystem hostSystem = (HostSystem) new InventoryNavigator(  
		                   rootFolder).searchManagedEntity("HostSystem", name);  
				 datastores=hostSystem.getDatastores();
			 }else if("Cluster".equals(type)){
				 ClusterComputeResource cluster = (ClusterComputeResource) new InventoryNavigator(  
		                   rootFolder).searchManagedEntity("ClusterComputeResource", name);  
				 datastores=cluster.getDatastores();
			 }if("Datacenter".equals(type)){
				 Datacenter datacenter = (Datacenter) new InventoryNavigator(  
		                   rootFolder).searchManagedEntity("Datacenter", name);  
				 datastores=datacenter.getDatastores();
			 } 
             
         
           //指定服务器：HostSystem 关联的Datastore  
           
             for(int i=0;i<datastores.length;i++){  
               Datastore datastore =datastores[i];  
               HostDatastoreInfo hostDatastoreInfo= setDatastore(datastore,false);  
               list.add(hostDatastoreInfo);  
           }  
 
           //datastore 数量  
           System.out.println("size is:"+list.size());  
           for(int j=0;j<list.size();j++){  
               HostDatastoreInfo datastoreInfo=list.get(j);  
               System.out.println("------------start-----------------------");  
               System.out.println("name:"+datastoreInfo.getName());  
               System.out.println("------------end-----------------------");  
           }  
	        result=JSONUtils.obj2json(list) ;
		} catch (Exception e) {
			LOGGER.error("getClusterDetail error:",e);
		}
         return result ;
	}
 
	/**  
     * 获取HostSystem存储信息  
     * @param datastore  
     * @return  
     */  
    public HostDatastoreInfo setDatastore(Datastore datastore,boolean isDiscoveryVmFile)  
    {  
        HostDatastoreInfo hostDatastore = new HostDatastoreInfo();  
        hostDatastore.setName(datastore.getSummary().getName());  
        hostDatastore.setUrl(datastore.getSummary().getUrl());  
        hostDatastore.setMaxFileSize(datastore.getInfo().getMaxFileSize());  
        hostDatastore.setFreeSpace(datastore.getSummary().getFreeSpace());  
        hostDatastore.setTimestamp(datastore.getInfo().getTimestamp());  
        hostDatastore.setCapacity(datastore.getSummary().getCapacity()); //容量  
        hostDatastore.setProgId(datastore.getMOR().val);  
        hostDatastore.setAccessible("0"); //连接状态  
        hostDatastore.setDsType(datastore.getSummary().getType());  
        if(datastore.getSummary().isAccessible())  
            hostDatastore.setAccessible("1");  
        if(datastore.getHost()!=null)  
            for(int j=0;j<datastore.getHost().length;j++)  
            {  
                HostMountInfo hostMountInfo = datastore.getHost()[j].getMountInfo();  
                HostDatastoreMountInfo hostDatastoreMount = new HostDatastoreMountInfo();  
                hostDatastoreMount.setPath(hostMountInfo.getPath());  
                hostDatastoreMount.setAccessible(hostMountInfo.getAccessible());  
                hostDatastoreMount.setAccessMode(hostMountInfo.getAccessMode());  
                hostDatastoreMount.setMounted(hostMountInfo.getMounted());  
                hostDatastore.setHostMount(hostDatastoreMount);  
            }  
        LOGGER.info("getDatastoreVmFile");
        if(isDiscoveryVmFile) //是否发现数据源虚拟文件  
        {  
            ArrayList<VirtualVmFile> vmFiles = getDatastoreVmFile(datastore);  
            hostDatastore.setVmFiles(vmFiles);  
        }  
        return hostDatastore;  
    }  
  
  
    /**  
     * 得取数据存储下面虚拟机文件,也就是虚拟磁盘  
     * @param datastore  
     * @return  
     */  
  
    @SuppressWarnings("deprecation")
	public ArrayList<VirtualVmFile> getDatastoreVmFile(Datastore datastore){  
        ArrayList<VirtualVmFile> result = new ArrayList<VirtualVmFile>();  
        try{  
  
            HostDatastoreBrowser dsBrowser = datastore.getBrowser();  
            DatastoreSummary ds =  datastore.getSummary();  
            String dsName = ds.getName();  
            VmDiskFileQueryFilter vdiskFilter = new VmDiskFileQueryFilter();  
            vdiskFilter.setControllerType(new String [] {});  
            VmDiskFileQuery fQuery = new VmDiskFileQuery();  
            fQuery.setFilter(vdiskFilter);  
            HostDatastoreBrowserSearchSpec searchSpec = new HostDatastoreBrowserSearchSpec();  
            searchSpec.setQuery(new FileQuery []{fQuery});  
            Task task = dsBrowser.searchDatastoreSubFolders_Task("["+dsName+"]", searchSpec);  
            task.waitForMe();  
            TaskInfo tInfo = task.getTaskInfo();  
            ArrayOfHostDatastoreBrowserSearchResults searchResult = (ArrayOfHostDatastoreBrowserSearchResults)tInfo.getResult();  
            int len = searchResult.getHostDatastoreBrowserSearchResults().length;  
            for(int j=0 ; j<len; j++)  
            {  
                HostDatastoreBrowserSearchResults sres =  searchResult.HostDatastoreBrowserSearchResults[j];  
                FileInfo [] fileArray = sres.getFile();  
                if(fileArray == null) continue;  
                for(int k=0 ; k<fileArray.length; k++)  
                {  
                    VirtualVmFile vmFile = new VirtualVmFile();  
                    vmFile.setFileSize(fileArray[k].getFileSize());  
                    vmFile.setModification(fileArray[k].getModification());  
                    vmFile.setOwner(fileArray[k].getOwner());  
                    vmFile.setPath(fileArray[k].getPath());  
                    result.add(vmFile);  
                }  
            }  
        }catch(Exception e)  
        {  
        	LOGGER.error("getDatastoreVmFile error:",e);
        }  
        return result;  
    }  
  
}  
 