package com.cqut.service;

import java.util.List;
import java.util.Map;

public interface IRole_assignmentService {
	public List<Map<String,Object>> getRole();
	
	public List<Map<String,Object>> getOperator();
	
	public List<Map<String,Object>> getOldPerm(String user_id);
	
	public Map<String,Object> delPerm(String user_id,String ids);
	
	public List<Map<String,Object>> upPermission(String user_id,String role_ids);
}
