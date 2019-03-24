package com.cqut.icode.asss_android.entity;

import java.util.Date;

/**
 * 作者：hwl
 * 时间：2017/7/3:22:10
 * 邮箱：1097412672@qq.com
 * 说明：
 */
public class Terminal {
    private String id;
    private String code;
    private String data_card;
    private String terminal_name;
    private String area_id;
    private String type;
    private String project_id;
    private String personliable;
    private String personliable_phone;
    private Date install_time;
    private float longitude;
    private int isguarantee;
    private String picture1;
    private String picture2;
    private String picture3;
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getType() {
        return type;
    }

    public void setType_id(String type) {
        this.type = type;
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

    public int getIsguarantee() {
        return isguarantee;
    }

    public void setIsguarantee(int isguarantee) {
        this.isguarantee = isguarantee;
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
}
