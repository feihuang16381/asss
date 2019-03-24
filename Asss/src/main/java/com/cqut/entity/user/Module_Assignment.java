package com.cqut.entity.user;

import com.cqut.entity.base.Entity;

public class Module_Assignment extends Entity{
	private String id;
	private String module_id;
	private String permission_id;
	private String remark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModule_id() {
		return module_id;
	}

	public void setModule_id(String module_id) {
		this.module_id = module_id;
	}

	public String getPermission_id() {
		return permission_id;
	}

	public void setPermission_id(String permission_id) {
		this.permission_id = permission_id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "Module_Assignment [id=" + id + ", module_id=" + module_id
				+ ", permission_id=" + permission_id + ", remark=" + remark
				+ "]";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "Module_Assignment";
	}

	@Override
	public String getPrimaryKey() {
		// TODO Auto-generated method stub
		return "id";
	}

}
