package com.dzd.sdn.vmware.entity;

public class ResultInfo {

	
	private String result;	//处理结果码，0-成功；1-失败；
	private String resultInfo;	//处理结果描述信息
	private String errorInfo;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getResultInfo() {
		return resultInfo;
	}
	public void setResultInfo(String resultInfo) {
		this.resultInfo = resultInfo;
	}
	public ResultInfo(String result, String resultInfo) {
		super();
		this.result = result;
		this.resultInfo = resultInfo;
	}
	public ResultInfo() {
		super();
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	@Override
	public String toString() {
		return "ResultInfo [result=" + result + ", resultInfo=" + resultInfo + ", errorInfo=" + errorInfo + "]";
	}
	public ResultInfo(String result, String resultInfo, String errorInfo) {
		super();
		this.result = result;
		this.resultInfo = resultInfo;
		this.errorInfo = errorInfo;
	}
	
}
