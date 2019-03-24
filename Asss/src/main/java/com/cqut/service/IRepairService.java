package com.cqut.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;

public interface IRepairService {
	public List<Map<String, Object>> getWorkload(String department_id);
	public String addRepairReport(JSONObject json, MultipartFile[] files, HttpServletRequest request);
	public List<Map<String, Object>> getRepairReport(int row, int page,String user_id, String task_name ,String terminal_name, String repair_type);
	public Map<String, Object> getRepairDetails(String repair_id, int repair_type);
	public List<Map<String, Object>> getAllTask();
	public List<Map<String, Object>> getRepairStatis();
	int deleteRecord(String Record_id);
}
