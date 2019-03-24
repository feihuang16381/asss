package com.cqut.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cqut.dao.base.EntityDao;
import com.cqut.dao.base.SearchDao;
import com.cqut.entity.user.Department;
import com.cqut.entity.user.Module_Assignment;
import com.cqut.entity.user.Permission;
import com.cqut.entity.user.Permission_Assignment;
import com.cqut.entity.user.User;
import com.cqut.util.EntityIDFactory;

@Service("departmentService")
public class DepartmentService implements IDepartmentService{
	@Resource(name = "entityDao")
    private EntityDao entityDao;
	@Resource(name = "searchDao")
	private SearchDao searchDao;
	
	@Override
	public Map<String, Object> getDepart(int page, int row, String name) {
		String condition = " 1=1 ";
		String join = "LEFT JOIN (select department_id,group_concat(permission_id) as permissions from permission_assignment group by department_id) as permission_assignment "
					+"on permission_assignment.department_id = department.id";
		if (name != null && !name.isEmpty())
			condition += " AND department.name like '%"
					+ name + "%'";
		List<Map<String, Object>> resultList = searchDao
				.searchWithpagingInMysql(
						new String[] { " department.id, "
									 + " department.name, "
									 + " department.phone_number, "
									 + " department.remark,"
									 + " permission_assignment.permissions"},
						" department ", join, null, null, condition,
						null, "department.id", "ASC", row, page);
		int count = searchDao.getForeignCount(" department.id ", " department ", null, null, null, condition);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", resultList);
		map.put("total", count);
		return map;
	}

	@Override
	public Map<String, Object> addDepart(Map<String, Object> department) {
		Department departments = new Department();
		String id = EntityIDFactory.createId();
		departments.setId(id);
		departments.setProperties(department);
				
		Map<String, Object> result = new HashMap<String, Object>();;
		String resultString = entityDao.save(departments) == 1 ? "true": "false";
		result.put("departmentsResult", resultString);
		
		//存储部门选中的权限
		@SuppressWarnings("unchecked")
		List<String> assList = (List<String>) department.get("permissions");
		for (int i = 0; i < assList.size(); i++) {
			Permission_Assignment pa = new Permission_Assignment();
			pa.setId(EntityIDFactory.createId());
			pa.setDepartment_id(id);
			pa.setPermission_id(assList.get(i));
			
			String result1 = entityDao.save(pa) == 1 ? "true": "false";
			result.put("paResult" + i, result1);
		}
		return result;
	}

	@Override
	public Map<String, Object> updateDepart(String id,
			Map<String, Object> department) {
		Department departments = entityDao.getByID(id, Department.class);
		Map<String,Object> result = new HashMap<String,Object>();

		departments.setProperties(department);
		String resultString = entityDao.updatePropByID(departments,
				id) == 1 ? "true" : "false";
		//先删除该部门的所有权限，在增加该部门的权限
		if(deleteAllPA(id)){
			//存储部门选中的权限
			@SuppressWarnings("unchecked")
			List<String> assList = (List<String>) department.get("assList");
			for (int i = 0; i < assList.size(); i++) {
				Permission_Assignment pa = new Permission_Assignment();
				pa.setId(EntityIDFactory.createId());
				pa.setDepartment_id(id);
				pa.setPermission_id(assList.get(i));
				
				String result1 = entityDao.save(pa) == 1 ? "true": "false";
				result.put("paResult" + i, result1);
			}
		}
		
		result.put("result", resultString);
		return result;
	}

	@Override
	public int deleteDepart(String department_id) {
		String[] ids = department_id.split(",");
		int BackValue = 0;
		for(String id:ids){
			if(deleteAllPA(department_id))
				BackValue += entityDao.deleteByID(id, Department.class);
		}
		if(BackValue != ids.length){
			BackValue = BackValue - ids.length;
		}
		//返回删除成功的个数
	    return BackValue;
	}

	public boolean deleteAllPA(String department_id){
		return entityDao.deleteByCondition(" department_id = " + department_id, Permission_Assignment.class) == 1;
	}
	
	@Override
	public List<Map<String, Object>> getAllPermission() {
		String condition = " 1=1 ";
		List<Map<String, Object>> list = searchDao.searchForeign(
				new String[]{"permission.id",
							 "permission.permission_name"}, 
				"permission", null, null, null, condition);
		return list;
	}

}
