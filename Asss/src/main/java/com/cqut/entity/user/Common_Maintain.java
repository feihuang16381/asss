package com.cqut.entity.user;
import com.cqut.entity.base.Entity;
public class Common_Maintain extends Entity{
	private String id;
	private String description;
	private String repair_report_id;
	private String repace_facilitys;
	private int is_handle;
	private String remark;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRepair_report_id() {
		return repair_report_id;
	}

	public void setRepair_report_id(String repair_report_id) {
		this.repair_report_id = repair_report_id;
	}

	public String getRepace_facilitys() {
		return repace_facilitys;
	}

	public void setRepace_facilitys(String repace_facilitys) {
		this.repace_facilitys = repace_facilitys;
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
		return "Common_Maintain [id=" + id + ", description=" + description
				+ ", repair_report_id=" + repair_report_id
				+ ", repace_facilitys=" + repace_facilitys + ", is_handle="
				+ is_handle + ", remark=" + remark + "]";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "Common_Maintain";
	}

	@Override
	public String getPrimaryKey() {
		// TODO Auto-generated method stub
		return "id";
	}
}
