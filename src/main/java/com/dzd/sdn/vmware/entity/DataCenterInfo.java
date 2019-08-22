package com.dzd.sdn.vmware.entity;

/**  
 * 数据中心  
 * @author zhb  
 */  

public class DataCenterInfo extends Entity{  
    private String name;   //数据中心名称  
    private String vmFolterName;  //虚拟机目录名称  
    private String vmFolterProgId; //虚拟机目录PROGID  
    private String networkFolder; //网络目录名称  
    private String networkFolderProgId;//网络目录PROGID  
    private String datastoreFolderName; //数据存储目录名称  
    private String datastoreFolderProgId;//存储目录PROGID  
    private String hostFolderName;  //主机目录名称  
    private String hostFolderProgId; //主机目录PROGID  
    private Integer dcId; //数据库ID  

    //临时信息  
    private String parentFolderProgId; //父目录PROGID  

    public String getDatastoreFolderName() {  
        return datastoreFolderName;  
    }  
    public void setDatastoreFolderName(String datastoreFolderName) {  
        this.datastoreFolderName = datastoreFolderName;  
    }  
    public String getHostFolderName() {  
        return hostFolderName;  
    }  
    public void setHostFolderName(String hostFolderName) {  
        this.hostFolderName = hostFolderName;  
    }  
    public String getName() {  
        return name;  
    }  
    public void setName(String name) {  
        this.name = name;  
    }  
    public String getNetworkFolder() {  
        return networkFolder;  
    }  
    public void setNetworkFolder(String networkFolder) {  
        this.networkFolder = networkFolder;  
    }  
    public String getVmFolterName() {  
        return vmFolterName;  
    }  
    public void setVmFolterName(String vmFolterName) {  
        this.vmFolterName = vmFolterName;  
    }  
    public String getParentFolderProgId() {  
        return parentFolderProgId;  
    }  
    public void setParentFolderProgId(String parentFolderProgId) {  
        this.parentFolderProgId = parentFolderProgId;  
    }  
    public String getDatastoreFolderProgId() {  
        return datastoreFolderProgId;  
    }  
    public void setDatastoreFolderProgId(String datastoreFolderProgId) {  
        this.datastoreFolderProgId = datastoreFolderProgId;  
    }  
    public String getHostFolderProgId() {  
        return hostFolderProgId;  
    }  
    public void setHostFolderProgId(String hostFolderProgId) {  
        this.hostFolderProgId = hostFolderProgId;  
    }  
    public String getNetworkFolderProgId() {  
        return networkFolderProgId;  
    }  
    public void setNetworkFolderProgId(String networkFolderProgId) {  
        this.networkFolderProgId = networkFolderProgId;  
    }  
    public String getVmFolterProgId() {  
        return vmFolterProgId;  
    }  
    public void setVmFolterProgId(String vmFolterProgId) {  
        this.vmFolterProgId = vmFolterProgId;  
    }  
    public Integer getDcId() {  
        return dcId;  
    }  
    public void setDcId(Integer dcId) {  
        this.dcId = dcId;  
    }  
}  