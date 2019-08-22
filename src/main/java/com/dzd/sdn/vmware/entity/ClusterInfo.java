package com.dzd.sdn.vmware.entity;
/**  
 * 集群信息  
 * @author zhb  
 *  
 */  
public class ClusterInfo extends Entity{  
    private String name; //名称  
    //ha配置数据  
    private Boolean haEnabled;   //是否启用ha  
    private String haVmMonitoring;   //虚拟机监控状态  
    private String haHostMonitoring; //主机监控状态  
    private Integer haFailoverLevel; //主机故障数目  
  
    private boolean admisControlEnabled; //接入控制状态 1:表示允许接入控制    0:表示不允许接入控制  
    private Integer cpuFailoverPercent; //cpu故障切换百分比  
    private Integer memFailoverPercent; //内存故障切换百分比  
    private String restartPriority; //虚拟机重新启动优先级  
    private String isolationResponse;//主机隔离响应  
    private String dsCandidatePolicy;//数据存储检测信号  
    //drs配置数据  
    private boolean drsEnabled; //是否启用drs  
    private boolean drsEnableVmBehaviorOverrides; //启动个别虚拟机自动化级别  
    private String autoLevel;//自动化级别 manual partiallyAutomated fullyAutomated  
    private Integer dcId; //数据中心ID  
  
  
  
  
    public Boolean getHaEnabled() {  
        return haEnabled;  
    }  
    public void setHaEnabled(Boolean haEnabled) {  
        this.haEnabled = haEnabled;  
    }  
    public String getHaVmMonitoring() {  
        return haVmMonitoring;  
    }  
    public void setHaVmMonitoring(String haVmMonitoring) {  
        this.haVmMonitoring = haVmMonitoring;  
    }  
    public String getHaHostMonitoring() {  
        return haHostMonitoring;  
    }  
    public void setHaHostMonitoring(String haHostMonitoring) {  
        this.haHostMonitoring = haHostMonitoring;  
    }  
    public Integer getHaFailoverLevel() {  
        return haFailoverLevel;  
    }  
    public void setHaFailoverLevel(Integer haFailoverLevel) {  
        this.haFailoverLevel = haFailoverLevel;  
    }  
    public String getName() {  
        return name;  
    }  
    public void setName(String name) {  
        this.name = name;  
    }  
    public boolean isDrsEnabled() {  
        return drsEnabled;  
    }  
    public void setDrsEnabled(boolean drsEnabled) {  
        this.drsEnabled = drsEnabled;  
    }  
    public boolean isDrsEnableVmBehaviorOverrides() {  
        return drsEnableVmBehaviorOverrides;  
    }  
    public void setDrsEnableVmBehaviorOverrides(boolean drsEnableVmBehaviorOverrides) {  
        this.drsEnableVmBehaviorOverrides = drsEnableVmBehaviorOverrides;  
    }  
    public boolean isAdmisControlEnabled() {  
        return admisControlEnabled;  
    }  
    public void setAdmisControlEnabled(boolean admisControlEnabled) {  
        this.admisControlEnabled = admisControlEnabled;  
    }  
    public String getAutoLevel() {  
        return autoLevel;  
    }  
    public void setAutoLevel(String autoLevel) {  
        this.autoLevel = autoLevel;  
    }  
    public Integer getCpuFailoverPercent() {  
        return cpuFailoverPercent;  
    }  
    public void setCpuFailoverPercent(Integer cpuFailoverPercent) {  
        this.cpuFailoverPercent = cpuFailoverPercent;  
    }  
    public String getDsCandidatePolicy() {  
        return dsCandidatePolicy;  
    }  
    public void setDsCandidatePolicy(String dsCandidatePolicy) {  
        this.dsCandidatePolicy = dsCandidatePolicy;  
    }  
    public String getIsolationResponse() {  
        return isolationResponse;  
    }  
    public void setIsolationResponse(String isolationResponse) {  
        this.isolationResponse = isolationResponse;  
    }  
    public Integer getMemFailoverPercent() {  
        return memFailoverPercent;  
    }  
    public void setMemFailoverPercent(Integer memFailoverPercent) {  
        this.memFailoverPercent = memFailoverPercent;  
    }  
    public String getRestartPriority() {  
        return restartPriority;  
    }  
    public void setRestartPriority(String restartPriority) {  
        this.restartPriority = restartPriority;  
    }  
    public Integer getDcId() {  
        return dcId;  
    }  
    public void setDcId(Integer dcId) {  
        this.dcId = dcId;  
    }  
  
} 