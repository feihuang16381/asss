package com.cqut.service;

import java.util.List;
import java.util.Map;

public interface IPermissionService {

	public Map<String, Object> getPermLev(int row, int page,String name);

	public Map<String, Object> addPermLev(String module_ids,Map<String, Object> permLev);

	public Map<String, Object> delPermLevById(String permLevIds);

	public Map<String, Object> upPermLev(String id, Map<String, Object> permLev);

	List<Map<String, Object>> getMoudle();

	List<Map<String, Object>> upPermission(String permission_id,String module_ids);

	List<Map<String, Object>> getOldPerm(String level_id);

	Map<String, Object> delPerm(String permission_id,String ids);
}
