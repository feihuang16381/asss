package com.cqut.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cqut.dao.base.EntityDao;
import com.cqut.dao.base.SearchDao;
import com.cqut.entity.user.Area;
import com.cqut.entity.user.Project;
import com.cqut.util.EntityIDFactory;

@Service("projectService")
public class ProjectService implements IProjectService{
	@Resource(name = "entityDao")
    private EntityDao entityDao;
	@Resource(name = "searchDao")
	private SearchDao searchDao;
	
	@Override
	public Map<String, Object> getProject(int row, int page, String name) {
		String condition = " 1=1 ";
		if (name != null && !name.isEmpty())
			condition += " AND project.name like '%"
					+ name + "%'";
		List<Map<String, Object>> resultList = searchDao
				.searchWithpagingInMysql(
						new String[] { " project.id, "
								+ " project.name, "
								+ " project.owner_unit, "
								+ " project.phone_number, "
								+ " project.remark "},
						" project ", null, null, null, condition,
						null, "project.id", "ASC", row, page);
		int count = searchDao.getForeignCount(" project.id ", " project ", null, null, null, condition);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", resultList);
		map.put("total", count);
		return map;
	}

	@Override
	public Map<String, Object> addProject(Map<String, Object> project) {
		Project projects = new Project();
		String id = EntityIDFactory.createId();
		projects.setId(id);
		projects.setProperties(project);
		
		Map<String, Object> result = new HashMap<String, Object>();;
		String resultString = entityDao.save(projects) == 1 ? "true": "false";
		result.put("result", resultString);
		
		return result;
	}

	@Override
	public Map<String, Object> updateProject(String id,
			Map<String, Object> project) {
		Project projects = entityDao.getByID(id, Project.class);
		projects.setProperties(project);
		String resultString = entityDao.updatePropByID(projects,
				id) == 1 ? "true" : "false";
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("result", resultString);
		return result;
	}

	@Override
	public int deleteProject(String project_id) {
		String[] ids = project_id.split(",");
		int BackValue = 0;
		for(String id:ids){
			BackValue += entityDao.deleteByID(id, Project.class);
		}
		if(BackValue != ids.length){
			BackValue = BackValue - ids.length;
		}
		//返回删除成功的个数
	    return BackValue;
	}
	
}
