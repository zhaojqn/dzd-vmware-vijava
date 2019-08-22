package com.ustcinfo.sdn.vmware.entity;

/**  
 * 存储配置安装点的相关信息  
 * @author Administrator  
 *  
 */  
public class HostDatastoreMountInfo extends Entity{  
    private String path;  
    private String accessMode;//访问模式  
    private Boolean mounted;  //挂载状态  
    private Boolean accessible;  //数据存储是目前从主机访问  
  
    public String getPath() {  
        return path;  
    }  
    public void setPath(String path) {  
        this.path = path;  
    }  
    public String getAccessMode() {  
        return accessMode;  
    }  
    public void setAccessMode(String accessMode) {  
        this.accessMode = accessMode;  
    }  
    public Boolean getMounted() {  
        return mounted;  
    }  
    public void setMounted(Boolean mounted) {  
        this.mounted = mounted;  
    }  
    public Boolean getAccessible() {  
        return accessible;  
    }  
    public void setAccessible(Boolean accessible) {  
        this.accessible = accessible;  
    }  
}  