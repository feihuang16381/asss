package com.cqut.entity.user;
import java.util.Date;

import com.cqut.entity.base.Entity;
public class Task extends Entity{
	private String id;
	private String task_name;
	private String terminal_id;
	private String type;
	private String content;
	private int isComplete;
	private int isAudit;
	private Date time;
	private String founder;
	private String remark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}

	public String getTerminal_id() {
		return terminal_id;
	}

	public void setTerminal_id(String terminal_id) {
		this.terminal_id = terminal_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public int getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(int isComplete) {
		this.isComplete = isComplete;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getFounder() {
		return founder;
	}

	public void setFounder(String founder) {
		this.founder = founder;
	}
	
	public int getIsAudit() {
		return isAudit;
	}

	public void setIsAudit(int isAudit) {
		this.isAudit = isAudit;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", task_name=" + task_name + ", terminal_id="
				+ terminal_id + ", type=" + type + ", content=" + content
				+ ", time=" + time + ", founder=" + founder + ", remark="
				+ remark + "]";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "Task";
	}

	@Override
	public String getPrimaryKey() {
		// TODO Auto-generated method stub
		return "id";
	}

}
