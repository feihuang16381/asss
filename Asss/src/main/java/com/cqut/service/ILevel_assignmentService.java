package com.cqut.service;

import java.util.List;
import java.util.Map;

public interface ILevel_assignmentService {
	public List<Map<String,Object>> getRole();
	
	public List<Map<String,Object>> getLevel();
	
	public List<Map<String,Object>> getOldPerm(String role_id);
	
	public Map<String,Object> delPerm(String ids);
	
	public List<Map<String,Object>> upPermission(String role_id,List<Map<String,String>> level_ids);
}
