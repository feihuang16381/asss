package com.cqut.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface IMessageService {
	public Map<String, Object> getNoticeList(int row, int page, String name);
	public Map<String, Object> addNotice(Map<String, Object> notice);
	public Map<String, Object> updateNotice(String id, Map<String, Object> notice);
	public int deleteNotice(String id);	
	public Map<String, Object> getTasks(int row, int page, String name);
	public List<Map<String, Object>> getUser_ids(String task_id);
	public List<Map<String, Object>> getTerminalName();
	public Map<String, Object> getTaskList(int row, int page, String user_id, String name, String type, int typeNum);
	public Map<String, Object> addTask(Map<String, Object> task, HttpServletRequest request);
	public Map<String, Object> updateTask(String id, Map<String, Object> task, HttpSession httpSession);
	public int deleteTask(String id, HttpSession httpSession);
	public int isReceive(String task_id);
	public int auditTask(String id);
	public List<Map<String, Object>> getNoticeList2();
	public List<Map<String, Object>> getTaskList2();
	public int ReceiveTask(String id);
	public List<Map<String, Object>> DealData();
	public int ReadNotice(String noticeID);
	
	
	
}
