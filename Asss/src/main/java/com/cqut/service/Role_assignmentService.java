package com.cqut.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cqut.dao.base.EntityDao;
import com.cqut.dao.base.SearchDao;
import com.cqut.entity.user.Role_Assignment;
import com.cqut.util.EntityIDFactory;


@Service("role_assignmentService")
public class Role_assignmentService implements IRole_assignmentService {
	@Resource(name = "entityDao")
    private EntityDao entityDao;
	@Resource(name = "searchDao")
	private SearchDao searchDao;
	
	@Override
	public List<Map<String, Object>> getRole() {
		String[] properties = {
				"role_type.id",
				"role_type.type"
		};
		String tableName = "role_type";
		String condition = "1 = 1";
		
		List<Map<String,Object>> resultList = searchDao.searchWithpagingInMysql
				(properties,tableName,null,null,null,condition,null,"role_type.id", " DESC ", 1000, 1);

		return resultList;
    }

	@Override
	public List<Map<String, Object>> getOperator() {
		String[] properties = {
				"user.id",
				"user.user_account"
		};
		String tableName = "user";
		String condition = "1 = 1";
		
		List<Map<String,Object>> resultList = searchDao.searchWithpagingInMysql
				(properties,tableName,null,null,null,condition,null,"user.id", " DESC ", 1000
						, 1);
		return resultList;
	}

	@Override
	public List<Map<String, Object>> upPermission(String user_id,String role_ids) {
		Role_Assignment RA = new Role_Assignment();
		String[] ids = role_ids.split(",");
		
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		if(role_ids!=null){
			for(String role_id:ids){
				RA.setRole_id(role_id);
				String id = EntityIDFactory.createId();
				RA.setId(id);
				RA.setUser_id(user_id);
				
				String resultString = entityDao.save(RA) == 1 ? "true": "false";
				
				result.put("result", resultString);
				resultList.add(result);
				}
		}
		
		return resultList;
	}

	@Override
	public List<Map<String, Object>> getOldPerm(String user_id) {
		String[] properties = {
				"role_type.id",
				"role_type.type"
		};
		String tableName = "role_assignment";
		String condition = "1 = 1";
		if (user_id != null && user_id.compareTo("null") != 0){
			condition = "1 = 1 and role_assignment.user_id like '"+user_id+"'";
		}
		
		String joinEntity = " LEFT JOIN role_type on role_assignment.role_id = role_type.id ";
		
		List<Map<String,Object>> resultList = searchDao.searchWithpagingInMysql
				(properties,tableName,joinEntity,null,null,condition,null,"role_type.id", " DESC ", 1000
						, 1);

		return resultList;
	}

	@Override
	public Map<String, Object> delPerm(String user_id,String ids) {
		String[] Ids = ids.split(",");
		String[] properties = {
				"role_assignment.id"
		};
		String tableName = "role_assignment";
		
		Map<String,Object> moudle_ass = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		
		String condition = "1 = 1";
		for(String role_id:Ids){
			if (user_id != null && user_id.compareTo("null") != 0 && ids != null){
				condition = "1 = 1 and role_assignment.user_id like '"+user_id+"' and "
						+ "role_assignment.role_id LIKE '"+role_id+"'";
			}
			
			List<Map<String,Object>> resultList = searchDao.searchWithpagingInMysql
					(properties,tableName,null,null,null,condition,null,"role_assignment.id", " DESC ", 1000
							, 1);
			
			moudle_ass = resultList.get(0);
			int resultInt = entityDao.deleteByID(moudle_ass.get("id").toString(), Role_Assignment.class);
			
			result.put("result", resultInt);
		}	
		    return result; 
	}
}
