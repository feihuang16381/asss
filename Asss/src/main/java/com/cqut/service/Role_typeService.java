package com.cqut.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cqut.dao.base.EntityDao;
import com.cqut.dao.base.SearchDao;
import com.cqut.entity.user.Permission_Assignment;
import com.cqut.entity.user.Role_Assignment;
import com.cqut.entity.user.Role_Type;
import com.cqut.util.EntityIDFactory;


@Service("role_typeService")
public class Role_typeService implements IRole_typeService {
	@Resource(name = "entityDao")
    private EntityDao entityDao;
	@Resource(name = "searchDao")
	private SearchDao searchDao;
	
	@Override
	public Map<String, Object> getRole(int row,int page,String type) {
		String[] properties = {
				"role_type.id",
				"role_type.type",
				"role_type.remark"
		};
		String tableName = "role_type";
		String condition = "1 = 1";
		
		if (type != null && type.length() > 0){
			condition = "1 = 1 and role_type.type like '%"+type+"%'";
		}
		
		List<Map<String,Object>> resultList = searchDao.searchWithpagingInMysql
				(properties,tableName,null,null,null,condition,null,"role_type.id", " DESC ",row, page);
		
		int count = searchDao.getForeignCount(Role_Type.getPrimaryKey(Role_Type.class), tableName, null, null, null, condition);
		
		Map<String,Object> result = new HashMap<String,Object>();
	    result.put("data", resultList);
	    result.put("total", count);
		return result;
    }

	@Override
	public Map<String, Object> addRole(String parm_ids,Map<String, Object> roleinfo) {
		Role_Type role = new Role_Type();
		
		role.setType(roleinfo.get("type").toString());
		String role_id = EntityIDFactory.createId();
		role.setId(role_id);
		
		if(roleinfo.get("remark")!=null){
			role.setRemark(roleinfo.get("remark").toString());
		}
		
		Map<String, Object> result = new HashMap<String, Object>();;
		String resultString = entityDao.save(role) == 1 ? "true": "false";
		result.put("result", resultString);
		
		String[] permIds = parm_ids.split(",");
		if(parm_ids!=null && !permIds[0].equals("empty")){
			for(String id:permIds){
				Permission_Assignment pa = new Permission_Assignment();
				String pa_id = EntityIDFactory.createId();
				pa.setId(pa_id);
				pa.setPermission_id(id);
				//pa.setRole_type_id(role_id);
				String resultString2 = entityDao.save(pa) == 1 ? "true": "false";
				result.put("parm_add", resultString2);
			}
		}
		
		return result;
	}

	@Override
	public Map<String, Object> delRoleById(String roleIds) {
		String[] RoleIds = roleIds.split(",");
        String[] properties = {"permission_assignment.id"};

        //从role_type表中删除一个或多个数据
		String resultString = entityDao.deleteEntities(RoleIds,
				Role_Type.class) >= 1 ? "true" : "false";

		for(int i = 0; i < RoleIds.length;i++){

			String condition = "permission_assignment.role_type_id like '"+RoleIds[i]+"'";

			List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
			result = searchDao.searchWithpagingInMysql(properties, "permission_assignment", null,
					null, null, condition, null, 
					null, null, 200, 1);
			for(Map<String,Object> res:result){
				String deleteResult = entityDao.deleteByID(res.get("id").toString(),
						Permission_Assignment.class) >= 1 ? "true" : "false";
			}

			//根据role_type_id，删除permission_assiginment表中该行所有数据
			entityDao.deleteByCondition("role_type_id like "+RoleIds[i],Permission_Assignment.class);
			//根据role_id，删除role_assiginment表中该行所有数据
			entityDao.deleteByCondition("role_id like "+RoleIds[i],Role_Assignment.class);
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", resultString);
		return result;
	}

	@Override
	public Map<String, Object> upRole(String id, Map<String, Object> roleinfo) {
		Role_Type role = entityDao.getByID(id, Role_Type.class);
		Map<String,Object> result = new HashMap<String,Object>();
		
		if(role==null){
			result.put("result","false");
		}
		role.setType(roleinfo.get("type").toString());
		if(roleinfo.get("remark")!=null){
			role.setRemark(roleinfo.get("remark").toString());
		}
		
		String resultString = entityDao.updatePropByID(role,
				id) == 1 ? "true" : "false";
		
		result.put("result", resultString);
		return result;
	}
	
	@Override
	public List<Map<String, Object>> getLevel() {
		String[] properties = {
				"permission.id",
				"permission.permission_name"
		};
		String tableName = "permission";
		String condition = "1 = 1";
		
		List<Map<String,Object>> resultList = searchDao.searchWithpagingInMysql
				(properties,tableName,null,null,null,condition,null,"permission.id", " DESC ", 1000
						, 1);
		return resultList;
	}

	@SuppressWarnings("unused")
	@Override
	public List<Map<String, Object>> upPermission(String role_id,String permission_ids) {
		Permission_Assignment PA = new Permission_Assignment();
		String[] pis = permission_ids.split(",");
		
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		
		if(permission_ids!=null){
			for(String permission_id:pis){
				//PA.setRole_type_id(role_id);
				PA.setPermission_id(permission_id);
				String id = EntityIDFactory.createId();
				PA.setId(id);
				
				String resultString = entityDao.save(PA) == 1 ? "true": "false";
				
				result.put("result", resultString);
				resultList.add(result);
				}
		}
		else{
			result.put("status", "false");
			resultList.add(result);
			return resultList;
		}
		
		return resultList;
	}

	@Override
	public List<Map<String, Object>> getOldPerm(String role_id) {
		String[] properties = {
				"permission.id",
				"permission.permission_name"
		};
		String tableName = "permission_assignment";
		String condition = "1 = 1";
		if (role_id != null && role_id.compareTo("null") != 0){
			condition = "1 = 1 and permission_assignment.role_type_id like '"+role_id+"'";
		}
		
		String joinEntity = " LEFT JOIN permission on permission_assignment.permission_id = permission.id ";
		
		List<Map<String,Object>> resultList = searchDao.searchWithpagingInMysql
				(properties,tableName,joinEntity,null,null,condition,null,"permission_assignment.id", " DESC ", 1000
						, 1);

		return resultList;
	}

	@Override
	public Map<String, Object> delPerm(String role_id,String ids) {
        String[] Ids = ids.split(",");
		String[] properties = {
				"permission_assignment.id"
		};
		String tableName = "permission_assignment";
		Map<String,Object> perm_ass = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		
		String condition = "1 = 1";
		for(String perm_id:Ids){
			if (role_id != null && role_id.compareTo("null") != 0 && ids != null){
				condition = "1 = 1 and permission_assignment.role_type_id like '"+role_id+"' and "
						+ "permission_assignment.permission_id LIKE '"+perm_id+"'";
			}
			
			List<Map<String,Object>> resultList = searchDao.searchWithpagingInMysql
					(properties,tableName,null,null,null,condition,null,"permission_assignment.id", " DESC ", 1000
							, 1);
			
			perm_ass = resultList.get(0);
			int resultInt = entityDao.deleteByID(perm_ass.get("id").toString(), Permission_Assignment.class);
			
			result.put("result", resultInt);
		}	
		    return result; 
	}

}
