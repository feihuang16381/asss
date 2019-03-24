package com.cqut.entity.user;

import com.cqut.entity.base.Entity;

public class Type extends Entity{
	private String id;
	private String type_name;
	private String remark;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "Type [id=" + id + ", type_name=" + type_name + ", remark="
				+ remark + "]";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "Type";
	}

	@Override
	public String getPrimaryKey() {
		// TODO Auto-generated method stub
		return "id";
	}

}
