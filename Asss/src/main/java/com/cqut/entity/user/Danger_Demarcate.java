package com.cqut.entity.user;
import com.cqut.entity.base.Entity;
public class Danger_Demarcate extends Entity{
	private String id;
	private String repair_report_id;
	private double before_demarcate;
	private double after_demarcate;
	private int is_handle;
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

	public double getBefore_demarcate() {
		return before_demarcate;
	}

	public void setBefore_demarcate(double before_demarcate) {
		this.before_demarcate = before_demarcate;
	}

	public double getAfter_demarcate() {
		return after_demarcate;
	}

	public void setAfter_demarcate(double after_demarcate) {
		this.after_demarcate = after_demarcate;
	}

	public int getIs_handle() {
		return is_handle;
	}

	public void setIs_handle(int is_handle) {
		this.is_handle = is_handle;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "Danger_Demarcate [id=" + id + ", repair_report_id="
				+ repair_report_id + ", before_demarcate=" + before_demarcate
				+ ", after_demarcate=" + after_demarcate + ", is_handle="
				+ is_handle + ", remark=" + remark + "]";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "Danger_Demarcate";
	}

	@Override
	public String getPrimaryKey() {
		// TODO Auto-generated method stub
		return "id";
	}

}
