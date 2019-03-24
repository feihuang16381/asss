package com.cqut.entity.user;
import java.util.Date;

import com.cqut.entity.base.Entity;
public class Repair_Report extends Entity{
	private String id;
	private String task_id;
	private String picture_before;
	private String picture_now;
	private String picture_after;
	private String repair_type;
	private Date repair_time;
	private String remark;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getPicture_before() {
		return picture_before;
	}

	public void setPicture_before(String picture_before) {
		this.picture_before = picture_before;
	}

	public String getPicture_now() {
		return picture_now;
	}

	public void setPicture_now(String picture_now) {
		this.picture_now = picture_now;
	}

	public String getPicture_after() {
		return picture_after;
	}

	public void setPicture_after(String picture_after) {
		this.picture_after = picture_after;
	}

	public String getRepair_type() {
		return repair_type;
	}

	public void setRepair_type(String repair_type) {
		this.repair_type = repair_type;
	}

	public Date getRepair_time() {
		return repair_time;
	}

	public void setRepair_time(Date repair_time) {
		this.repair_time = repair_time;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	@Override
	public String getTableName() {
		return "Repair_Report";
	}

	@Override
	public String getPrimaryKey() {
		return "id";
	}

}
