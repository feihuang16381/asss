package com.cqut.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cqut.dao.base.BaseEntityDao;
import com.cqut.dao.base.EntityDao;
import com.cqut.dao.base.SearchDao;

@Service
public class AttendanceService implements IAttendanceService{
	@Resource(name = "entityDao")
    private EntityDao entityDao;
	@Resource(name = "searchDao")
	private SearchDao searchDao;
	@Resource(name = "baseEntityDao")
	private BaseEntityDao baseEntityDao;
	public List<Map<String, Object>> getAttendance(){
		String[] properties = {  "latitude" ,"DATE_FORMAT(attendanceTime, '%Y/%m/%d %h:%m:%s') as attendanceTime","user_name","longitude"};
		String baseEntity = "asss.user a";
		String joinEntity = " join asss.user  ON a.id = user.id";
		String condition = "";
	/*	String condition = " a.LoginID = '" + userName+"'";*/
		
		List<Map<String, Object>> list = searchDao.searchWithpagingInMysqlNoOrder(properties, baseEntity, joinEntity, null, null, null, null, 0, 5);
		System.out.println("list is:"+list);
		return list;
		
	}
	
	
	
	
	
}
