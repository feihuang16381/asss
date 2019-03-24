package com.cqut.entity.user;

import com.cqut.entity.base.Entity;

public class Department extends Entity{
	private String id;
	private String name;
	private String phone_number;
	private String remark;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "Department [id=" + id + ", name=" + name + ", phone_number="
				+ phone_number + ", remark=" + remark + "]";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "Department";
	}

	@Override
	public String getPrimaryKey() {
		// TODO Auto-generated method stub
		return "id";
	}

}
