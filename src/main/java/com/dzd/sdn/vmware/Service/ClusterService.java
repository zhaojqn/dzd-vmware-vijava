package com.dzd.sdn.vmware.Service;

import java.util.ArrayList;

import com.dzd.sdn.vmware.entity.ClusterInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dzd.sdn.util.JSONUtils;
import com.vmware.vim25.ClusterFailoverResourcesAdmissionControlPolicy;
import com.vmware.vim25.mo.ClusterComputeResource;
import com.vmware.vim25.mo.Datacenter;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;

@Service
public class ClusterService {
	private static final Logger LOGGER=LoggerFactory.getLogger(ClusterService.class);
	@Autowired
	private ClientSesion clientSesion;
	//192.168.80.22
	/**  
	 * 数据集群发现  
	 * @param serviceInstance  
	 * @return  
	 */  
	public String  getClusterDetail() {
		  String result="";
		  ArrayList<ClusterInfo> list = new ArrayList<ClusterInfo>();
		 try {
			 Folder rootFolder = clientSesion.getServiceInstance().getRootFolder();  
				
 	        ManagedEntity[] clusters = new InventoryNavigator(rootFolder).searchManagedEntities("ClusterComputeResource" );  
	        for(int i=0;i<clusters.length;i++)  
	        {  
	            ClusterComputeResource cluster = (ClusterComputeResource)clusters[i];  
	            ClusterInfo clusterInfo = getClusterInfo(cluster);  
	            //被充信息  
	            list.add(clusterInfo);  
	        }  
	        result=JSONUtils.obj2json(list) ;
		} catch (Exception e) {
			LOGGER.error("getClusterDetail error:",e);
		}
         return result ;
	}
 
	/**  
	 * 数据集群发现  
	 * @param serviceInstance  
	 * @return  
	 */  
	public String  getClusterDetailByDcName(String dcName) {
		  String result="";
		  ArrayList<ClusterInfo> list = new ArrayList<ClusterInfo>();  
		 try {
			 Folder rootFolder = clientSesion.getServiceInstance().getRootFolder();  
				
	        Datacenter dc = (Datacenter) new InventoryNavigator(rootFolder).searchManagedEntity("Datacenter", dcName);  
	        ManagedEntity[] clusters =  new InventoryNavigator(dc).searchManagedEntities("ClusterComputeResource");  
	        for(int i=0;i<clusters.length;i++)  
	        {  
	            ClusterComputeResource cluster = (ClusterComputeResource)clusters[i];  
	            ClusterInfo clusterInfo = getClusterInfo(cluster);  
	            //被充信息  
	            list.add(clusterInfo);  
	        }  
	        result=JSONUtils.obj2json(list) ;
		} catch (Exception e) {
			LOGGER.error("getClusterDetail error:",e);
		}
         return result ;
	}
 


 

/**  
 * 得取集群信息  
 * @param cluster  
 * @return  
 */  
public ClusterInfo getClusterInfo(ClusterComputeResource cluster){  
    ClusterInfo clusterInfo = new ClusterInfo();  
    clusterInfo.setName(cluster.getName());  
    clusterInfo.setHaEnabled(cluster.getConfiguration().getDasConfig().getEnabled());  
    clusterInfo.setHaFailoverLevel(cluster.getConfiguration().getDasConfig().getFailoverLevel());  
    clusterInfo.setHaHostMonitoring(cluster.getConfiguration().getDasConfig().getHostMonitoring());  
    clusterInfo.setHaVmMonitoring(cluster.getConfiguration().getDasConfig().getVmMonitoring());  
    clusterInfo.setAdmisControlEnabled(cluster.getConfiguration().getDasConfig().getAdmissionControlEnabled());  
    if(cluster.getConfiguration().getDasConfig().getAdmissionControlPolicy() instanceof ClusterFailoverResourcesAdmissionControlPolicy)  
    {  
        ClusterFailoverResourcesAdmissionControlPolicy policy = (ClusterFailoverResourcesAdmissionControlPolicy)cluster.getConfiguration().getDasConfig().getAdmissionControlPolicy();  
        clusterInfo.setCpuFailoverPercent(policy.getCpuFailoverResourcesPercent());  
        clusterInfo.setMemFailoverPercent(policy.getMemoryFailoverResourcesPercent());  
    }  
    clusterInfo.setRestartPriority(cluster.getConfiguration().getDasConfig().getDefaultVmSettings().getRestartPriority());  
    clusterInfo.setIsolationResponse(cluster.getConfiguration().getDasConfig().getDefaultVmSettings().getIsolationResponse());  
    clusterInfo.setDsCandidatePolicy(cluster.getConfiguration().getDasConfig().getHBDatastoreCandidatePolicy());  
    clusterInfo.setDrsEnabled(cluster.getConfiguration().getDrsConfig().getEnabled());  
    clusterInfo.setDrsEnableVmBehaviorOverrides(cluster.getConfiguration().getDrsConfig().getEnableVmBehaviorOverrides());  
    clusterInfo.setAutoLevel(cluster.getConfiguration().getDrsConfig().getDefaultVmBehavior().name());  
    clusterInfo.setProgId(cluster.getMOR().val);  
    return clusterInfo;  
}  

} 