package com.cqut.entity.user;
import com.cqut.entity.base.Entity;

public class Role_Type extends Entity{
	private String id;
	private String type;
	private String remark;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "Role_Type [id=" + id + ", type=" + type + ", remark=" + remark
				+ "]";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "Role_Type";
	}

	@Override
	public String getPrimaryKey() {
		// TODO Auto-generated method stub
		return "id";
	}

}
