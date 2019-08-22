package com.ustcinfo.sdn.vmware.entity;

import java.util.Calendar;

/**  
 * 虚拟文件  
 * @author zhb  
 *  
 */  
public class VirtualVmFile {  
    private String path;  
    private Long fileSize;  
    private Calendar modification;  
    private String owner;  
  
    public String getPath() {  
        return path;  
    }  
    public void setPath(String path) {  
        this.path = path;  
    }  
    public Long getFileSize() {  
        return fileSize;  
    }  
    public void setFileSize(Long fileSize) {  
        this.fileSize = fileSize;  
    }  
    public Calendar getModification() {  
        return modification;  
    }  
    public void setModification(Calendar modification) {  
        this.modification = modification;  
    }  
    public String getOwner() {  
        return owner;  
    }  
    public void setOwner(String owner) {  
        this.owner = owner;  
    }  
}  