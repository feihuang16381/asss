package com.cqut.service;

import java.util.List;
import java.util.Map;

public interface IModuleService {

	public Map<String, Object> getModule(int row, int page, String name);

	public Map<String, Object> addModule(Map<String, Object> module);

	public Map<String, Object> delModuleById(String moduleIds);

	public Map<String, Object> upModule(String id, Map<String, Object> module);

	public List<Map<String, Object>> getFMoudle();
}
