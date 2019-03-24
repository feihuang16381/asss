package com.cqut.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.cqut.dao.base.EntityDao;
import com.cqut.dao.base.SearchDao;
import com.cqut.entity.user.Area;
import com.cqut.entity.user.Spare_part;
import com.cqut.entity.user.Spare_part_Application;
import com.cqut.entity.user.Spare_part_Record;
import com.cqut.util.EntityIDFactory;

@Service("sparepartService")
public class SparepartService implements ISparepartService{
	@Resource(name = "entityDao")
    private EntityDao entityDao;
	@Resource(name = "searchDao")
	private SearchDao searchDao;
	
	@Override
	public Map<String, Object> getSparepart(int row, int page,
			String name) {
		String condition = " 1=1 ";
		if (name != null && !name.isEmpty())
			condition += " AND spare_part.name like '%"
					+ name + "%'";
		List<Map<String, Object>> resultList = searchDao
				.searchWithpagingInMysql(
						new String[] { " spare_part.id, "
								+ " spare_part.name, "
								+ " spare_part.type, "
								+ " spare_part.remark"},
						" spare_part ", null, null, null, condition,
						null, "spare_part.id", "ASC", row, page);
		int count = searchDao.getForeignCount(" spare_part.id ", " spare_part ", null, null, null, condition);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data",resultList);
		map.put("total", count);
		return map;
	}
	
	public List<Map<String,Object>> getSparts(){
		return searchDao.searchForeign(
				new String[] { " spare_part.id, "
				+ " spare_part.name, "
				+ " spare_part.remark "}, 
				" spare_part ", null, null, null, null);
	}
	@Override
	public Map<String, Object> addSparepart(Map<String, Object> sparepart, HttpSession session) {
		Spare_part spareparts = new Spare_part();
		String id = EntityIDFactory.createId();
		spareparts.setId(id);
		spareparts.setProperties(sparepart);
		
		Map<String, Object> result = new HashMap<String, Object>();;
		String resultString = entityDao.save(spareparts) == 1 ? "true": "false";
		result.put("result", resultString);
		
		return result;
	}
	@Override
	public Map<String, Object> updateSparepart(String id,
			Map<String, Object> sparepart) {
		Spare_part spareparts = entityDao.getByID(id, Spare_part.class);
		spareparts.setProperties(sparepart);
		String resultString = entityDao.updatePropByID(spareparts,
				id) == 1 ? "true" : "false";
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("result", resultString);
		return result;
	}
	@Override
	public int deleteSparepart(String sparepart_id) {
		String[] ids = sparepart_id.split(",");
		int BackValue = 0;
		for(String id:ids){
			BackValue += entityDao.deleteByID(id, Spare_part.class);
		}
		if(BackValue != ids.length){
			BackValue = BackValue - ids.length;
		}
		//返回删除成功的个数
	    return BackValue;
	}
	
	@Override
	public Map<String, Object> getSpare_Record(int row, int page,
			String name) {
		String condition = " 1=1 ";
		if (name != null && !name.isEmpty())
			condition += " AND user.user_name like '%"+ name + "%'"
			+ " or task.task_name like '%"+ name + "%'"
			+ " or spare_part_application.apply_name like '%"+ name + "%'";
		String joinEntity = " LEFT JOIN user ON user.id = spare_part_application.user_id "
				+ " LEFT JOIN task ON task.id = spare_part_application.task_id ";
		List<Map<String, Object>> resultList = searchDao
				.searchWithpagingInMysql(
						new String[] { " spare_part_application.id, "
								+ " spare_part_application.is_read, "
								+ " user.id as user_id, "
								+ " task.id as task_id, "
								+ " spare_part_application.apply_name, "
								+ " user.user_name, "
								+ " task.task_name, "
								+ " DATE_FORMAT(spare_part_application.apply_time, '%Y-%c-%d %H:%i:%s') as apply_time, "
								+ " spare_part_application.remark "},
						" spare_part_application ", joinEntity, null, null, condition,
						null, "apply_time", "DESC", row, page);
		int count = searchDao.getForeignCount(" spare_part_application.id ", " spare_part_application ", joinEntity, null, null, condition);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", resultList);
		map.put("total", count);
		return map;
	}
	
	@Override
	public List<Map<String, Object>> getTaskName() {
		String condition = " isComplete = 0";
		List<Map<String, Object>> resultList = searchDao.searchForeign(
			new String[]{" task.id, task.task_name, task.remark "}, "task", null, null, null, condition);
		return resultList;
	}
	
	@Override
	public Map<String, Object> addRecord(Map<String, Object> sparepart_application, HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		Spare_part_Application spare_appl = new Spare_part_Application();
		String sparepart_application_id = EntityIDFactory.createId();
		String user_id = (String) session.getAttribute("user_id");
		spare_appl.setId(sparepart_application_id);
		spare_appl.setApply_time(new Date());
		spare_appl.setIs_read(0);
		spare_appl.setUser_id(user_id);
		spare_appl.setProperties(sparepart_application);

		@SuppressWarnings("unchecked")
		List<String> list = (List<String>) sparepart_application.get("sparepart_id");
		@SuppressWarnings("unchecked")
		List<Integer> numberlist = (List<Integer>) sparepart_application.get("number");
		if(list == null || numberlist == null){
			result.put("result", "备件未选择");
			return result;
		}
		for (int i = 0; i < list.size(); i++) {
			Spare_part_Record sparepart_record = new Spare_part_Record();
			String id = EntityIDFactory.createId();
			sparepart_record.setId(id);
			sparepart_record.setSparepart_application_id(sparepart_application_id);
			sparepart_record.setSparepart_id(list.get(i));
			if(numberlist.size() > i && numberlist.get(i) != null)
				sparepart_record.setNumber(numberlist.get(i));
			if(entityDao.save(sparepart_record) != 1){
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("result", "false");
				return map;
			}

		}
		String resultString = entityDao.save(spare_appl)  == 1 ? "true": "false";
		result.put("result", resultString);
	    return result;
	}
	@Override
	public Map<String, Object> updateRecord(String id, String sparepart_id,String sparepart_application_id,
			Map<String, Object> sparepart_application) {
		Map<String, Object> result = new HashMap<String, Object>();
		String apply_time = sparepart_application.get("apply_time").toString();
		sparepart_application.remove("apply_time");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
			try {
				date = df.parse(apply_time);
				Spare_part_Application spare_appl = entityDao.getByID(sparepart_application_id, Spare_part_Application.class);
				spare_appl.setApply_time(date);
				spare_appl.setProperties(sparepart_application);
				
				Spare_part_Record sparepart_record =  entityDao.getByID(id, Spare_part_Record.class);
				sparepart_record.setSparepart_id(sparepart_id);
				
				String resultString = entityDao.updatePropByID(spare_appl,
						sparepart_application_id) + entityDao.updatePropByID(sparepart_record,
								id) == 2 ? "true" : "false";
				result.put("result", resultString);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		 return result;
	}
	@Override
	public int deleteRecord(String record_id) {
		String[] ids = record_id.split(",");
		int BackValue = 0;
		for(String id:ids){
			BackValue += entityDao.deleteByID(id, Spare_part_Application.class);
		}
		if(BackValue != ids.length){
			BackValue = BackValue - ids.length;
		}
		//返回删除成功的个数
	    return BackValue;
	}

	@Override
	public int auditSparepart(String id) {
		Spare_part_Application spa = new Spare_part_Application();
		spa.setId(id);
		spa.setIs_read(1);
		return entityDao.updatePropByID(spa, id);
	}
}
