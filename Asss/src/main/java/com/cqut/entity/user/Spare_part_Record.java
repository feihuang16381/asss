package com.cqut.entity.user;
import com.cqut.entity.base.Entity;
public class Spare_part_Record extends Entity{
	private String id;
	private String sparepart_id;
	private String sparepart_application_id;
	private Integer number;
	private String remark;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSparepart_id() {
		return sparepart_id;
	}

	public void setSparepart_id(String sparepart_id) {
		this.sparepart_id = sparepart_id;
	}

	public String getSparepart_application_id() {
		return sparepart_application_id;
	}

	public void setSparepart_application_id(String sparepart_application_id) {
		this.sparepart_application_id = sparepart_application_id;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "Spare_part_Record [id=" + id + ", sparepart_id=" + sparepart_id
				+ ", sparepart_application_id=" + sparepart_application_id
				+ ", number=" + number + ", remark=" + remark + "]";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "Spare_part_Record";
	}

	@Override
	public String getPrimaryKey() {
		// TODO Auto-generated method stub
		return "id";
	}

}
