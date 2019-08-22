package com.ustcinfo.sdn.vmware.Service;

import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ustcinfo.sdn.config.ClientConfig;
import com.vmware.vim25.AboutInfo;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.ServiceInstance;
@Service
public class ClientSesion  {
	private static final Logger LOGGER=LoggerFactory.getLogger(ClientSesion.class);

	private  String isConnected = "disConnect"; //disConnect Connect   Connectting
 	private ServiceInstance serviceInstance=null;
	
    public ServiceInstance getServiceInstance() {
		return serviceInstance;
	}

	public void setServiceInstance(ServiceInstance serviceInstance) {
		this.serviceInstance = serviceInstance;
	}
	@Autowired
	private ClientConfig config;
 

	public  synchronized void connect() {
		isConnected = "Connectting";
  		try{  
            
            URL url = new URL("https", config.getClientIp(), "/sdk");  
            serviceInstance = new ServiceInstance(url, config.getClientName(), config.getClientPassword(), true);         
            AboutInfo ai = serviceInstance.getAboutInfo(); 
            LOGGER.info("名称:{};版本:{};apiType{}",ai.getFullName(),ai.getVersion(),ai.apiType);              
            isConnected = "Connect";
		} catch (Exception e) {
			LOGGER.error("连接异常:", e);
		}
 	}

	public  synchronized void disconnect() {
		try {
			if (isConnected.equals("Connect")) {
				serviceInstance.getServerConnection().logout();
				LOGGER.info("断开连接");
 			}
			 isConnected = "disConnect";
 		} catch (Exception e) {
			LOGGER.error("关闭连接异常:", e);
		}
	}
 

	public void KeepAlive(){
		try {
    		KeepAlive k=new KeepAlive();
		   	 Thread thread = new Thread(k);
		   	 thread.setName("Vmware连接检测线程");
		   	 thread.start();
		} catch (Exception e) {
			LOGGER.error("Vmware连接检测失败",e);
		}
	}
	
	
	
	public  boolean check(   ) {
		try {
 			 LOGGER.info("检查 信息{}",serviceInstance.currentTime());
  		}catch (Exception e) {
			LOGGER.error("检查 异常",e);	
			 return false;
		}
		return true;
	}
	class KeepAlive implements Runnable {
		 @Override
			public void run() {
				while (true) {
					try {
						LOGGER.info("检查 连接状态");
						if (!isConnected.equals("Connectting")){
							if(check()){
								LOGGER.info("检查 连接状态成功，休眠{}毫秒",config.getInterval());
								Thread.sleep(config.getInterval());
							}else{
								LOGGER.info("检查 连接状态:断开连接，重新连接。");
								 connect();
							}
							
						}else{
							LOGGER.info("正在连接，休眠{}毫秒",1000);
							Thread.sleep(1000);
						}

					} catch (Exception t) {
						LOGGER.error("检查 异常",t);	
					}
				}

			} 
	 }
 
	
	
	
	
}
