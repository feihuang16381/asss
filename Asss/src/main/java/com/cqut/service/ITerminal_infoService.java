package com.cqut.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface ITerminal_infoService {

	public Map<String, Object> getTerminal(int row, int page, String code_name,String type_id);

	public List<Map<String, Object>> getType();
	
	public List<Map<String, Object>> getArea();
	
	public List<Map<String, Object>> getProject();
	
	public Map<String, Object> addTerminal(Map<String, Object> Terminal, MultipartFile[] files);

	public Map<String, Object> delTerminalById(String terminalIds);

	public Map<String, Object> upTerminal(String id, Map<String, Object> Terminal);

	public Map<String, Object> getTerminal(Map<String, Object> condition);

	public Map<String, Object> getTerminalMessage(String terminalId);
}
