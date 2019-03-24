package com.cqut.service;

import java.util.List;
import java.util.Map;

public interface IProjectService {
	public Map<String, Object> getProject(int row, int page, String name);
	public Map<String, Object> addProject(Map<String, Object> project);
	public Map<String, Object> updateProject(String id, Map<String, Object> project);
	public int deleteProject(String id);
}
