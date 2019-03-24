package com.cqut.icode.asss_android.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：hwl
 * 时间：2017/7/29:16:48
 * 邮箱：1097412672@qq.com
 * 说明：
 */
public class CommonUtil {

    /**
     * Method:cloneData
     * Description:将传入对象中的内容克隆出副本
     * Creator:hwl
     * Date:2017/7/2916:11
     */
    public static void cloneData(List<Map<String, Object>> oriData,List<Map<String, Object>> clonalData){
        clonalData.clear();
        for(int i = 0;i < oriData.size();i++){
            HashMap<String,Object> map = new HashMap<>();
            map.putAll(oriData.get(i));
            clonalData.add(map);
        }
    }


}
