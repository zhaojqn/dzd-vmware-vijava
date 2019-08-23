package com.dzd.sdn.vmware.Service;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vmware.vim25.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dzd.sdn.util.JSONUtils;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.PerformanceManager;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;
@Service
public class VmService {
	private static final Logger LOGGER=LoggerFactory.getLogger(VmService.class);
	@Autowired
	private ClientSesion clientSesion;
	//new-安徽电信自动化运维平台01_80.103
	public  String getVMByName(String vmName){
		 String result="";
		 try {
			 Folder rootFolder = clientSesion.getServiceInstance().getRootFolder();  
				
	         ManagedEntity[] mes =new InventoryNavigator(rootFolder).searchManagedEntities("VirtualMachine");  
	       
	         LOGGER.info("VirtualMachine numbers:" + mes.length);  
	         if(mes!=null&& mes.length>0){
	        	 for (int i = 0; i < mes.length; i++) {
	        		 VirtualMachine virtualMachine=(VirtualMachine)mes[i];
	        		 if(virtualMachine.getConfig().getName().equals(vmName)){
	        			 VirtualMachineSummary smm=virtualMachine.getSummary();
	        			 VirtualHardware  vh= virtualMachine.getConfig().getHardware() ;
                         GuestInfo guest = virtualMachine.getGuest();
                         String hostName = guest.getHostName();
                         VirtualMachineRuntimeInfo runtime = virtualMachine.getRuntime();
                         ManagedEntityStatus configStatus = virtualMachine.getConfigStatus();

                         Map<String,Object> map = new HashMap<>(13);
//	        			 map.put("summary",smm);
//	        			 map.put("VirtualHardware",vh);
	        			 map.put("guest",guest);
	        			 result=JSONUtils.obj2json(map );
	        		 }
				}
	         }
	        
			 
		} catch (Exception e) {
			LOGGER.error("getVmdetail error:",e);
		}
         return result ;
 
	}
	
	
	public String  getVmdetail() {
		  String result="";
		 try {
			 Folder rootFolder = clientSesion.getServiceInstance().getRootFolder();  
				
	         ManagedEntity[] mes =new InventoryNavigator(rootFolder).searchManagedEntities("VirtualMachine");  
	       
	         LOGGER.info("VirtualMachine numbers:" + mes.length);  

	         VirtualMachine virtualMachine=(VirtualMachine)mes[0];  
	         VirtualMachineConfigInfo config=virtualMachine.getConfig();//虚拟机配置信息  
	         VirtualMachineCapability capability=virtualMachine.getCapability();//虚拟机容量信息  
	         VirtualMachineConfigSummary sum=virtualMachine.getSummary().getConfig();
	        /*  //虚拟机名称   
	         System.out.println("virtualMachine Name:" + virtualMachine.getName());  
	        //虚拟机名称  
	         System.out.println("virtualMachine Name:"+config.getName());  
	         //虚拟机描述  
	         System.out.println("virtualMachind desciption:" + config.getAnnotation());  
	         //虚拟机关联CPU(关联Process 或者)  
	         VirtualMachineAffinityInfo affinity=config.getCpuAffinity();  
	        //虚拟机版本信息  
	         System.out.println("virtualMachine version" + config.getVersion());  
	         //虚拟机CPU 计数器是否启用  
	         System.out.println("cpu counters enable:"+config.vPMCEnabled);  */
	         result=JSONUtils.obj2json(sum );
			 
		} catch (Exception e) {
			LOGGER.error("getVmdetail error:",e);
		}
         return result ;
	}
	
	
	
	public List getVMCPUPerByTimeRange(String vcomName,int timeRange,String instanceName)   {  
		  ServiceInstance instance = null;  
	     InventoryNavigator inventoryNavigator = null;  
		List result  = new ArrayList();  
        try {
        	instance = clientSesion.getServiceInstance();  
            inventoryNavigator = new InventoryNavigator(instance.getRootFolder());  
            ManagedEntity obj = inventoryNavigator.searchManagedEntity("VirtualMachine", vcomName);  
            if (null == obj)  
                throw new Exception();  
            com.vmware.vim25.mo.VirtualMachine vm = (com.vmware.vim25.mo.VirtualMachine) obj;  
            PerformanceManager pfMgr = instance.getPerformanceManager();  
            PerfProviderSummary summary = pfMgr.queryPerfProviderSummary(vm);  
            PerfInterval[] rfRates = pfMgr.getHistoricalInterval();  
            PerfMetricId[] pfMtIds = pfMgr.queryAvailablePerfMetric(vm, null, null, timeRange);  
            List<Map> contids = getCpuUsageAndUsageMhzCounterIds(pfMgr,vm,timeRange);  
            if( null != contids && contids.size() > 0 ) {  
                int[] counterIds = new int[pfMtIds.length];  
                int iLabel = 0;  
                for (PerfMetricId pfMt : pfMtIds) {  
                    int contId = pfMt.getCounterId();  
                    counterIds[iLabel++] = contId;  
                }  
                List<Map> instances = new ArrayList<Map>();  
                for (PerfMetricId mtId : pfMtIds) {  
                    int counterId = mtId.getCounterId();  

                    for (Map m : contids) {  
                        Integer cntId = (Integer) m.get("counterId");  
                        String groupName = (String) m.get("groupName");  
                        String targetName = (String) m.get("targetName");  
                        if (counterId == cntId && (null == instanceName || (null != instanceName && mtId.getInstance().equals(instanceName)))) {  
                            Map mm = new HashMap();  
                            mm.put("counterId", cntId);  
                            mm.put("instanceId", mtId.getInstance());  
                            mm.put("groupName", groupName);  
                            mm.put("targetName", targetName);  
                            instances.add(mm);  
                        }  


                    }  

                }  
                
                LOGGER.info("instances={}", instances);
                for (int i = 0; i < instances.size(); i++) {  
                    PerfMetricId[] cpuCores = new PerfMetricId[1];  
                    Map m = instances.get(i);  
                    cpuCores[0] = new PerfMetricId();  
                    cpuCores[0].setCounterId((Integer) m.get("counterId"));  
                    cpuCores[0].setInstance((String) m.get("instanceId"));  
                    PerfQuerySpec qSpec = createPerfQuerySpec(vm, cpuCores, 1, timeRange);  
                    PerfEntityMetricBase[] pValues = pfMgr.queryPerf(new PerfQuerySpec[]{qSpec});  
                    Map resultM = new HashMap();  
                    String values = "";  
                    String lables = "";  
                    String point = "" + m.get("targetName");  
                    PerfMetricSeriesCSV[] csvs = null;  
                    String name = "" + m.get("targetName") + "" + m.get("groupName");  
                    for (int j = 0; j < pValues.length; j++) {  
                        PerfEntityMetricCSV csvValue = (PerfEntityMetricCSV) pValues[j];  

                        csvs = csvValue.getValue();  
                        lables = csvValue.getSampleInfoCSV();  
                        for (int k = 0; k < csvs.length; k++) {  
                            values += csvs[k].getValue() + ",";  
                        }  

                    }
                    resultM.put("groupName", m.get("groupName"));  
                    resultM.put("targetName", m.get("targetName"));  
                    resultM.put("name", name);  
                    resultM.put("values", values);  
                    resultM.put("lables", lables);  
                    resultM.put("instance", instanceName);  
                    resultM.put("point", point);  
                    resultM.put("csvs", csvs); 
                    LOGGER.info("csvs={},resultM={}", csvs,JSONUtils.obj2json(resultM));
                    result.add(resultM);  
                }  
            }  
		} catch (Exception e) {
			e.printStackTrace();
		}
        LOGGER.info("result={}", result);
        return result;  
    }  

    public PerfQuerySpec createPerfQuerySpec(ManagedEntity me,  
                                             PerfMetricId[] metricIds, int maxSample, int interval)  
    {  
        PerfQuerySpec qSpec = new PerfQuerySpec();  
        qSpec.setEntity(me.getMOR());  
        // set the maximum of metrics to be return  
        // only appropriate in real-time performance collecting  
        qSpec.setMaxSample(new Integer(maxSample));  
        qSpec.setMetricId(metricIds);  
        // optionally you can set format as "normal"  
        qSpec.setFormat("csv");  
        //  qSpec.setFormat("normal");  
        // set the interval to the refresh rate for the entity  
        qSpec.setIntervalId(new Integer(interval));  

        return qSpec;  
    }  

    public List<Map> getCpuUsageAndUsageMhzCounterIds(PerformanceManager pfMgr,ManagedEntity managedEntity,Integer point) throws RemoteException {  
        List<Map> result = new ArrayList<Map>();  
        PerfMetricId[] pfMtIds = pfMgr.queryAvailablePerfMetric(managedEntity, null, null, point);  
        if(null != pfMtIds && pfMtIds.length > 0 ) {  
            int []counterIds = new int[pfMtIds.length];  
            int iLabel = 0;  
            for(PerfMetricId pfMt: pfMtIds){  
                int contId = pfMt.getCounterId();  
                counterIds[iLabel++] = contId;  
            }  
            
            LOGGER.info("counterIds={}",counterIds);

            PerfCounterInfo[] pfContinfos = pfMgr.queryPerfCounter(counterIds);  
            List<String> gpList = new ArrayList<String>();  
            for(int i = 0 ; i<  pfContinfos.length ; i++) {  
                String strName = pfContinfos[i].getNameInfo().getKey();  
                String groupName = pfContinfos[i].getGroupInfo().getKey();  
               // if (null != groupName && groupName.equals("cpu")) {  
                 //   if (null != strName && strName.equals("usage") || strName.equals("usagemhz")) {  
                LOGGER.info("counterIds[i]={},groupName={},strName={},pfContinfos[i].getKey()={}",counterIds[i],groupName,strName,pfContinfos[i].getKey());
                        if (result.size() > 0) {  
                            boolean flag = true;  
                            for (Map mm : result) {  
                                Integer key = (Integer) mm.get("counterId");  
                                if (key.equals(pfContinfos[i].getKey()))  
                                    flag = false;  
                            }  
                            if (flag) {  
                                Map m = new HashMap();  
                                m.put("counterId", counterIds[i]);  
                                m.put("groupName", groupName);  
                                m.put("targetName", strName);  
                                result.add(m);  
                            }  

                        } else {  
                            Map m = new HashMap();  

                            m.put("counterId", counterIds[i]);  
                            m.put("groupName", groupName);  
                            m.put("targetName", strName);  
                            result.add(m);  
                        }  
                    }  
                }  
        //    }  
      //  }  
        LOGGER.info("result={}", result);

        return result;  
    }  
}
