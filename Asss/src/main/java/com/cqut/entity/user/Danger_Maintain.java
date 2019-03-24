package com.cqut.entity.user;
import com.cqut.entity.base.Entity;
public class Danger_Maintain extends Entity{
	private String id;
	private String repair_report_id;
	private String fault_description;
	private String repace_facilitys;
	private int is_recover;
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

	public String getFault_description() {
		return fault_description;
	}

	public void setFault_description(String fault_description) {
		this.fault_description = fault_description;
	}

	public String getRepace_facilitys() {
		return repace_facilitys;
	}

	public void setRepace_facilitys(String repace_facilitys) {
		this.repace_facilitys = repace_facilitys;
	}

	public int getIs_recover() {
		return is_recover;
	}

	public void setIs_recover(int is_recover) {
		this.is_recover = is_recover;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "Danger_Maintain [id=" + id + ", repair_report_id="
				+ repair_report_id + ", fault_description=" + fault_description
				+ ", repace_facilitys=" + repace_facilitys + ", is_recover="
				+ is_recover + ", remark=" + remark + "]";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "Danger_Maintain";
	}

	@Override
	public String getPrimaryKey() {
		// TODO Auto-generated method stub
		return "id";
	}

}
