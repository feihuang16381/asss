package com.cqut.service;

import java.util.List;
import java.util.Map;

public interface IRole_typeService {

	public Map<String, Object> getRole(int row, int page, String type);

	public Map<String, Object> addRole(String parm_ids,Map<String, Object> role);

	public Map<String, Object> delRoleById(String roleIds);

	public Map<String, Object> upRole(String id, Map<String, Object> role);

	List<Map<String, Object>> getLevel();

	List<Map<String, Object>> upPermission(String role_id,
			String level_ids);

	List<Map<String, Object>> getOldPerm(String role_id);

	Map<String, Object> delPerm(String role_id,String ids);
}
