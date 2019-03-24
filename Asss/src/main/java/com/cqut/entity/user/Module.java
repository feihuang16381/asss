package com.cqut.entity.user;
import com.cqut.entity.base.Entity;

public class Module extends Entity{
	private String id;
	private String name;
	private String parent;
	private int haschild;
	private String level0;
	private int is_endofmoduleLevel;
	private String modulecode;
	private String url;
	private String remark;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public int getHaschild() {
		return haschild;
	}

	public void setHaschild(int haschild) {
		this.haschild = haschild;
	}

	public String getLevel0() {
		return level0;
	}

	public void setLevel0(String level0) {
		this.level0 = level0;
	}

	public int getIs_endofmoduleLevel() {
		return is_endofmoduleLevel;
	}

	public void setIs_endofmoduleLevel(int is_endofmoduleLevel) {
		this.is_endofmoduleLevel = is_endofmoduleLevel;
	}

	public String getModulecode() {
		return modulecode;
	}

	public void setModulecode(String modulecode) {
		this.modulecode = modulecode;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "Module [id=" + id + ", name=" + name + ", parent=" + parent
				+ ", haschild=" + haschild + ", level0=" + level0
				+ ", is_endofmoduleLevel=" + is_endofmoduleLevel
				+ ", modulecode=" + modulecode + ", remark=" + remark + "]";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "Module";
	}

	@Override
	public String getPrimaryKey() {
		// TODO Auto-generated method stub
		return "id";
	}

}
