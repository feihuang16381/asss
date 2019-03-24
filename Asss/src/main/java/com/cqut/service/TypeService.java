package com.cqut.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cqut.dao.base.EntityDao;
import com.cqut.dao.base.SearchDao;
import com.cqut.entity.user.Module;
import com.cqut.entity.user.Role_Type;
import com.cqut.entity.user.Type;
import com.cqut.util.EntityIDFactory;

@Service("typeService")
public class TypeService implements ITypeService{
	@Resource(name = "entityDao")
    private EntityDao entityDao;
	@Resource(name = "searchDao")
	private SearchDao searchDao;
	
	@Override
	public Map<String, Object> getType(int row, int page, String name) {
		String[] properties = {
				"type.id",
				"type.type_name",
				"type.remark"
		};
		String tableName = "type";
		String condition = "1 = 1";
		
		if (name != null && name.compareTo("null") != 0){
			condition = "1 = 1 and type.type_name like '%"+name+"%'";
		}
		
		List<Map<String,Object>> resultList = searchDao.searchWithpagingInMysql
				(properties,tableName,null,null,null,condition,null,"type.id", " DESC ", row
						, page);
		
		int count = searchDao.getForeignCount(Type.getPrimaryKey(Type.class), tableName, null, null, null, condition);		
		
		Map<String,Object> result = new HashMap<String,Object>();
	    result.put("data", resultList);
	    result.put("total", count);
		return result;
    }

	@Override
	public Map<String, Object> addType(Map<String, Object> type) {
		Type type1 = new Type();
		
		type1.setType_name(type.get("type_name").toString());
		String id = EntityIDFactory.createId();
		type1.setId(id);
		
		if(type.get("remark")!=null){
			type1.setRemark(type.get("remark").toString());
		}
		
		Map<String, Object> result = new HashMap<String, Object>();;
		String resultString = entityDao.save(type1) == 1 ? "true": "false";
		result.put("result", resultString);
		
		return result;
	}

	@Override
	public Map<String, Object> delTypeById(String typeIds) {
		String[] TypeIds = typeIds.split(",");
		
		String resultString = entityDao.deleteEntities(TypeIds,
				Type.class) >= 1 ? "true" : "false";
				
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", resultString);
		return result;
	}

	@Override
	public Map<String, Object> upType(String id, Map<String, Object> type) {
		Type type1 = entityDao.getByID(id, Type.class);
		Map<String,Object> result = new HashMap<String,Object>();
		
		if(type1==null){
			result.put("result","false");
		}
		
		type1.setType_name(type.get("type_name").toString());
		
		if(type.get("remark")!=null){
			type1.setRemark(type.get("remark").toString());
		}
		
		String resultString = entityDao.updatePropByID(type1,
				id) == 1 ? "true" : "false";
		
		result.put("result", resultString);
		return result;
	}

}
