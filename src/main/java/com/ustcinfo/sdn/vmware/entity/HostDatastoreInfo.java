package com.ustcinfo.sdn.vmware.entity;

import java.util.ArrayList;
import java.util.Calendar;

/**  
 * 存储信息  
 * @author zhb  
 */  
  
public class HostDatastoreInfo extends Entity{  
    private String name; //名称  
    private String url;  //定位数据存储  
    private long freeSpace;  //剩余空间容量 单位:byte  
    private long maxFileSize; //最大文件容量 单位:byte  
    private long capacity;//容量  
    private String accessible; //连接状态  
    private String uuid;//特定id  
    private Calendar timestamp; //空间剩余空间更新时间  
    private ArrayList<HostDatastoreMountInfo> hostMounts = new ArrayList<HostDatastoreMountInfo>();//挂载主机相关信息  
    private ArrayList<VirtualVmFile> vmFiles = new ArrayList<VirtualVmFile>();//虚拟机文件  
    private String dsType;//存储类型  
  
    public long getFreeSpace() {  
        return freeSpace;  
    }  
    public void setFreeSpace(long freeSpace) {  
        this.freeSpace = freeSpace;  
    }  
    public long getMaxFileSize() {  
        return maxFileSize;  
    }  
    public void setMaxFileSize(long maxFileSize) {  
        this.maxFileSize = maxFileSize;  
    }  
    public String getName() {  
        return name;  
    }  
    public void setName(String name) {  
        this.name = name;  
    }  
    public Calendar getTimestamp() {  
        return timestamp;  
    }  
    public void setTimestamp(Calendar timestamp) {  
        this.timestamp = timestamp;  
    }  
    public String getUrl() {  
        return url;  
    }  
    public void setUrl(String url) {  
        this.url = url;  
    }  
    public ArrayList<HostDatastoreMountInfo> getHostMounts() {  
        return hostMounts;  
    }  
    public void setHostMount(HostDatastoreMountInfo hostMount){  
        this.hostMounts.add(hostMount);  
    }  
  
    public void setHostMounts(ArrayList<HostDatastoreMountInfo> hostMounts) {  
        this.hostMounts = hostMounts;  
    }  
    public ArrayList<VirtualVmFile> getVmFiles() {  
        return vmFiles;  
    }  
    public void setVmFile(VirtualVmFile vmFile){  
        this.vmFiles.add(vmFile);  
    }  
    public void setVmFiles(ArrayList<VirtualVmFile> vmFiles) {  
        this.vmFiles = vmFiles;  
    }  
    public long getCapacity() {  
        return capacity;  
    }  
    public void setCapacity(long capacity) {  
        this.capacity = capacity;  
    }  
    public String getUuid() {  
        return uuid;  
    }  
    public void setUuid(String uuid) {  
        this.uuid = uuid;  
    }  
    public String getAccessible() {  
        return accessible;  
    }  
    public void setAccessible(String accessible) {  
        this.accessible = accessible;  
    }  
    public String getDsType() {  
        return dsType;  
    }  
    public void setDsType(String dsType) {  
        this.dsType = dsType;  
    }  
}  