package com.cqut.entity.user;
import com.cqut.entity.base.Entity;
public class Project extends Entity{
	private String id;
	private String name;
	private String owner_unit;
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

	public String getOwner_unit() {
		return owner_unit;
	}

	public void setOwner_unit(String owner_unit) {
		this.owner_unit = owner_unit;
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
		return "Project [id=" + id + ", name=" + name + ", owner_unit="
				+ owner_unit + ", phone_number=" + phone_number + ", remark="
				+ remark + "]";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "Project";
	}

	@Override
	public String getPrimaryKey() {
		// TODO Auto-generated method stub
		return "id";
	}

}
