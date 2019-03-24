package com.cqut.entity.user;
import com.cqut.entity.base.Entity;
public class Permission_Assignment extends Entity{
	private String id;
	private String department_id;
	private String permission_id;
	private String remark;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
		return "Permission_Assignment [id=" + id + ", department_id=" + department_id + ", permission_id="
				+ permission_id + ", remark=" + remark + "]";
	}

	public String getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(String department_id) {
		this.department_id = department_id;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "Permission_Assignment";
	}

	@Override
	public String getPrimaryKey() {
		// TODO Auto-generated method stub
		return "id";
	}
	
}
