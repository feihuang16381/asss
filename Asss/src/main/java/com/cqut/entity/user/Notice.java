package com.cqut.entity.user;
import java.util.Date;

import com.cqut.entity.base.Entity;
public class Notice extends Entity{
	private String id;
	private String title;
	private String content;
	private String founder;
	private Date create_time; 
	private String remark;
	private int state;
	
	
	public String getId() {
		return id;
	}

	

	public int getState() {
		return state;
	}



	public void setState(int state) {
		this.state = state;
	}



	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFounder() {
		return founder;
	}

	public void setFounder(String founder) {
		this.founder = founder;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "Notice [id=" + id + ", title=" + title + ", content=" + content
				+ ", founder=" + founder + ", create_time=" + create_time
				+ ", remark=" + remark + "]";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "Notice";
	}

	@Override
	public String getPrimaryKey() {
		// TODO Auto-generated method stub
		return "id";
	}

}
