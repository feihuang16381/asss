package com.cqut.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.cqut.service.IAttendanceService;

@Controller
public class AttendanceController {
	@Resource IAttendanceService AttendanceService;
	
	@RequestMapping(value="/getAttendance",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
	@ResponseBody
	public String getAttendance(){
		System.out.println(JSON.toJSONString(AttendanceService.getAttendance()));
		return JSON.toJSONString(AttendanceService.getAttendance());
		
	}
}
