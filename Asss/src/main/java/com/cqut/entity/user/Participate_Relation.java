package com.cqut.entity.user;
import com.cqut.entity.base.Entity;
public class Participate_Relation extends Entity{
	private String id;
	private String repair_report_id;
	private String user_id;
	private int is_writer;
	private String remark;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRepair_report_id() {
		return repair_report_id;
	}

	public void setRepair_report_id(String repair_report_id) {
		this.repair_report_id = repair_report_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public int getIs_writer() {
		return is_writer;
	}

	public void setIs_writer(int is_writer) {
		this.is_writer = is_writer;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "Participate_Relation [id=" + id + ", repair_report_id="
				+ repair_report_id + ", user_id=" + user_id + ", is_writer="
				+ is_writer + ", remark=" + remark + "]";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "Participate_Relation";
	}

	@Override
	public String getPrimaryKey() {
		// TODO Auto-generated method stub
		return "id";
	}

}
