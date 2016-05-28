package test.utils;


import com.alibaba.fastjson.JSONObject;

import test.model.Msger;

public class MsgTools {
	public static Msger parseJsonToMsger(String jsonString){
		
		JSONObject jsobj = JSONObject.parseObject(jsonString);
		
		Msger msger = jsobj.toJavaObject(Msger.class);
		return msger;
		
	}
	public static String parseObjectToJsonStr(Object poObject){
		
		return JSONObject.toJSONString(poObject);
		
	}
}
