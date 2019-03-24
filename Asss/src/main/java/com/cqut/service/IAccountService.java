package com.cqut.service;

import java.util.List;
import java.util.Map;

public interface IAccountService {
	public Map<String, Object> getAccount(int row, int page, String user_account);
	public int addAccount(Map<String, Object> account);
	public int updateAccount(String id, Map<String, Object> account);
	public int updatePassword(String id, String oldpassword, String newpassword);
	public Map<String, Object> updateState(String id, int state);
	public int deleteAccount(String id);
	public List<Map<String, Object>> getUserName();

}
