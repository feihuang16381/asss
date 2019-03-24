package com.cqut.service;

import java.util.List;
import java.util.Map;

public interface IAreaService {
	public Map<String, Object> getArea(int row, int page, String area_name);
	public Map<String, Object> addArea(Map<String, Object> area);
	public Map<String, Object> updateArea(String id, Map<String, Object> area);
	public int deleteArea(String id);
	
}
