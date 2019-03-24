package com.cqut.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cqut.dao.base.EntityDao;
import com.cqut.dao.base.SearchDao;
import com.cqut.entity.user.Area;
import com.cqut.entity.user.User;
import com.cqut.util.EntityIDFactory;

@Service("areaService")
public class AreaService implements IAreaService{
	@Resource(name = "entityDao")
    private EntityDao entityDao;
	@Resource(name = "searchDao")
	private SearchDao searchDao;
	
	@Override
	public Map<String, Object> getArea(int row, int page, String area_name) {
		String condition = " 1=1 ";
		if (area_name != null && !area_name.isEmpty())
			condition += " AND area.area_name like '%"
					+ area_name + "%'";
		List<Map<String, Object>> resultList = searchDao
				.searchWithpagingInMysql(
						new String[] { " area.id, "
								+ " area.area_name, "
								+ " area.remark"},
						" area ", null, null, null, condition,
						null, "area.id", "ASC", row, page);
		int count = searchDao.getForeignCount(" area.id ", " area ", null, null, null, condition);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data",resultList);
		map.put("total", count);
		return map;
	}
	
	@Override
	public Map<String, Object> addArea(Map<String, Object> area) {
		Area areas = new Area();
		String area_id = EntityIDFactory.createId();
		areas.setId(area_id);
		areas.setProperties(area);
		
		Map<String, Object> result = new HashMap<String, Object>();;
		String resultString = entityDao.save(areas) == 1 ? "true": "false";
		result.put("result", resultString);
		
		return result;
	}

	@Override
	public Map<String, Object> updateArea(String id, Map<String, Object> area) {
		Area areas = entityDao.getByID(id, Area.class);
		areas.setProperties(area);
		String resultString = entityDao.updatePropByID(areas,
				id) == 1 ? "true" : "false";
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("result", resultString);
		return result;
	}

	@Override
	public int deleteArea(String area_id) {
		String[] ids = area_id.split(",");
		int BackValue = 0;
		for(String id:ids){
			BackValue += entityDao.deleteByID(id, Area.class);
		}
		if(BackValue != ids.length){
			BackValue = BackValue - ids.length;
		}
		//返回删除成功的个数
	    return BackValue;
	}
}
