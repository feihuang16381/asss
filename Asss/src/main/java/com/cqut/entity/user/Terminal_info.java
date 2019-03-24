package com.cqut.entity.user;

import java.util.Date;

import com.cqut.entity.base.Entity;

public class Terminal_info extends Entity{
	private String id;
	private String station_code;
	private String data_card;
	private String terminal_name;
	private String area_id;
	private String type_id;
	private String project_id;
	private String personliable;
	private String personliable_phone;
	private Date install_time;
	private float longitude;
	private float latitude;
	private int is_guarantee;
	private String picture1;
	private String picture2;
	private String picture3;
	private String remark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStation_code() {
		return station_code;
	}

	public void setStation_code(String station_code) {
		this.station_code = station_code;
	}

	public String getData_card() {
		return data_card;
	}

	public void setData_card(String data_card) {
		this.data_card = data_card;
	}

	public String getTerminal_name() {
		return terminal_name;
	}

	public void setTerminal_name(String terminal_name) {
		this.terminal_name = terminal_name;
	}

	public String getArea_id() {
		return area_id;
	}

	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}

	public String getType_id() {
		return type_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public String getPersonliable() {
		return personliable;
	}

	public void setPersonliable(String personliable) {
		this.personliable = personliable;
	}

	public String getPersonliable_phone() {
		return personliable_phone;
	}

	public void setPersonliable_phone(String personliable_phone) {
		this.personliable_phone = personliable_phone;
	}

	public Date getInstall_time() {
		return install_time;
	}

	public void setInstall_time(Date install_time) {
		this.install_time = install_time;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public int getIs_guarantee() {
		return is_guarantee;
	}

	public void setIs_guarantee(int is_guarantee) {
		this.is_guarantee = is_guarantee;
	}

	public String getPicture1() {
		return picture1;
	}

	public void setPicture1(String picture1) {
		this.picture1 = picture1;
	}

	public String getPicture2() {
		return picture2;
	}

	public void setPicture2(String picture2) {
		this.picture2 = picture2;
	}

	public String getPicture3() {
		return picture3;
	}

	public void setPicture3(String picture3) {
		this.picture3 = picture3;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "Terminal_info [id=" + id + ", station_code=" + station_code
				+ ", data_card=" + data_card + ", terminal_name="
				+ terminal_name + ", area_id=" + area_id + ", type_id="
				+ type_id + ", project_id=" + project_id + ", personliable="
				+ personliable + ", personliable_phone=" + personliable_phone
				+ ", install_time=" + install_time + ", longitude=" + longitude
				+ ", latitude=" + latitude + ", is_guarantee=" + is_guarantee
				+ ", picture1=" + picture1 + ", picture2=" + picture2
				+ ", picture3=" + picture3 + ", remark=" + remark + "]";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "Terminal_info";
	}

	@Override
	public String getPrimaryKey() {
		// TODO Auto-generated method stub
		return "id";
	}

}
