/********************************************
* 文件名称: DocModel.java
* 系统名称: 现代金融综合仿真平台
* 模块名称: tbdocmodel合同文档模板
* 软件版权: 
* 功能说明: 
* 系统版本: 
* 开发人员: 现代金融综合仿真平台
* 开发时间:  
* 审核人员:
* 相关文档:
* 修改记录: 修改日期    修改人员    修改说明
*********************************************/
package com.wangxy.devkit.business.market;

import java.lang.*;

public class DocModel{
	//合同文档模板编号
	private String modelNo = " ";
	public String getModelNo(){
		return modelNo;
	}
	public void setModelNo(String modelNo){
		this.modelNo = modelNo;
	}

	//合同文档模板名称
	private String modelName = " ";
	public String getModelName(){
		return modelName;
	}
	public void setModelName(String modelName){
		this.modelName = modelName;
	}

	//文档名称
	private String docName = " ";
	public String getDocName(){
		return docName;
	}
	public void setDocName(String docName){
		this.docName = docName;
	}

	//服务器路径
	private String serverPath = " ";
	public String getServerPath(){
		return serverPath;
	}
	public void setServerPath(String serverPath){
		this.serverPath = serverPath;
	}

	//上传日期
	private int uploadDate = 0;
	public int getUploadDate(){
		return uploadDate;
	}
	public void setUploadDate(int uploadDate){
		this.uploadDate = uploadDate;
	}

	//录入人
	private String userId = " ";
	public String getUserId(){
		return userId;
	}
	public void setUserId(String userId){
		this.userId = userId;
	}

	//备注
	private String summary = " ";
	public String getSummary(){
		return summary;
	}
	public void setSummary(String summary){
		this.summary = summary;
	}


}
