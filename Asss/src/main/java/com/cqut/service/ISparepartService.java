package com.cqut.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

public interface ISparepartService {
	public Map<String, Object> getSparepart(int row, int page, String name);
	public Map<String, Object> addSparepart(Map<String, Object> sparepart, HttpSession httpSession);
	public Map<String, Object> updateSparepart(String id, Map<String, Object> sparepart);
	public int deleteSparepart(String id);
	public Map<String, Object> getSpare_Record(int row, int page, String name);
	public List<Map<String, Object>> getTaskName();
	public Map<String, Object> addRecord(Map<String, Object> sparepart_application, HttpSession httpSession);
	public Map<String, Object> updateRecord(String id, String sparepart_id, String sparepart_application_id, Map<String, Object> sparepart_application);
	public int deleteRecord(String id);
	public List<Map<String, Object>> getSparts();
	public int auditSparepart(String id);
}
