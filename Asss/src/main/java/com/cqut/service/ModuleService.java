package com.cqut.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cqut.dao.base.EntityDao;
import com.cqut.dao.base.SearchDao;
import com.cqut.entity.base.Entity;
import com.cqut.entity.user.Module;
import com.cqut.util.EntityIDFactory;

@Service("moduleService")
public class ModuleService implements IModuleService{
	@Resource(name = "entityDao")
    private EntityDao entityDao;
	@Resource(name = "searchDao")
	private SearchDao searchDao;
	
	@Override
	public Map<String, Object> getModule(int row, int page, String name) {
		String[] properties = {
				"module.id",
				"module.name",
				"parent.name as parent",
				"case when module.haschild = 0 then '否' else '是' end haschild",
				"module.level0",
				"case when module.is_endofmoduleLevel = 0 then '否' else '是' end is_endofmoduleLevel",
				"module.modulecode",
				"module.url",
				"module.remark"
		};
		String tableName = "module";
		String condition = "1 = 1";
		
		if (name != null && name.compareTo("null") != 0 && name.length() > 0){
			condition = "1 = 1 and module.name like '%"+name+"%'";
		}
		
		List<Map<String,Object>> resultList = searchDao.searchWithpagingInMysql
				(properties,
				 tableName,
				 " LEFT JOIN (SELECT id,name FROM module WHERE haschild = 0) as parent ON module.parent = parent.id ",
				 null,null,condition,null,"module.id", " DESC ", row , page);
		
		int count = searchDao.getForeignCount(Module.getPrimaryKey(Module.class), tableName, null, null, null, condition);		
		
		Map<String,Object> result = new HashMap<String,Object>();
	    result.put("data", resultList);
	    result.put("total", count);
		return result;
    }

	@Override
	public Map<String, Object> addModule(Map<String, Object> moduleinfo) {
		Module module = new Module();
		module.setProperties(moduleinfo);
		String module_id = EntityIDFactory.createId();
		module.setId(module_id);
		
		Map<String, Object> result = new HashMap<String, Object>();;
		String resultString = entityDao.save(module) == 1 ? "true": "false";
		result.put("result", resultString);
		
		return result;
	}

	@Override
	public Map<String, Object> delModuleById(String moduleIds) {
		String[] ModuleIds = moduleIds.split(",");
		
		String resultString = entityDao.deleteEntities(ModuleIds,
				Module.class) >= 1 ? "true" : "false";
				
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", resultString);
		return result;
	}

	@Override
	public Map<String, Object> upModule(String id, Map<String, Object> moduleinfo) {
		Module module = entityDao.getByID(id, Module.class);
		Map<String,Object> result = new HashMap<String,Object>();
		
		if(module==null){
			result.put("result","false");
			return result;
		}
		module.setProperties(moduleinfo);
		
		String resultString = entityDao.updatePropByID(module,
				id) == 1 ? "true" : "false";
		
		result.put("result", resultString);
		return result;
	}

	@Override
	public List<Map<String, Object>> getFMoudle() {
		List<Map<String, Object>>  list = entityDao.searchForeign(new String[]{"*"}, "module", null, null, " module.haschild = 0 ");
		return list;
	}

}
