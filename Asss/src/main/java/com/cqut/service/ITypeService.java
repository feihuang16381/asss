package com.cqut.service;

import java.util.List;
import java.util.Map;

public interface ITypeService {

	public Map<String, Object> getType(int row, int page, String name);

	public Map<String, Object> addType(Map<String, Object> type);

	public Map<String, Object> delTypeById(String typeIds);

	public Map<String, Object> upType(String id, Map<String, Object> type);
}
