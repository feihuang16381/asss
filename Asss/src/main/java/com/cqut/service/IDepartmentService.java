package com.cqut.service;

import java.util.List;
import java.util.Map;

public interface IDepartmentService {
	public Map<String, Object> getDepart(int page, int row, String name);
	public Map<String, Object> addDepart(Map<String, Object> department);
	public Map<String, Object> updateDepart(String id, Map<String, Object> department);
	public int deleteDepart(String id);
	public List<Map<String, Object>> getAllPermission();
}
