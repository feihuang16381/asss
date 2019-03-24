package com.cqut.entity.user;
import com.cqut.entity.base.Entity;
public class Area extends Entity{
	private String id;
	private String area_name;
	private String remark;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "Area [id=" + id + ", area_name=" + area_name + ", remark="
				+ remark + "]";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "Area";
	}

	@Override
	public String getPrimaryKey() {
		// TODO Auto-generated method stub
		return "id";
	}

}
