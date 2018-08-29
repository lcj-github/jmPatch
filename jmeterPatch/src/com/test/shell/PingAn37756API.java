package com.test.shell;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



public class PingAn37756API {
	
	public static String obtainkeyValue3775(String userName,String mobile,String marketCode,String mobileFormat,String remark) throws Exception
	{
		String key = "";
		String iv = "";
		
		Map<String,String> strMap  = new HashMap<String,String>();
		strMap.put("UserName", userName);		
		strMap.put("MarketCode", marketCode);
		strMap.put("Mobile", mobile);
		strMap.put("MobileFormat", mobileFormat);		
		strMap.put("Remark", remark);
		int mapSize = strMap.size();		
		String conStr  = "{";
		Iterator<String> keys = strMap.keySet().iterator(); 
		while(keys.hasNext()) {	
		   mapSize--;	
		   String keyMap = (String) keys.next(); 
		   String value=strMap.get(keyMap);
		   conStr = conStr+"\"";
		   conStr = conStr.concat(keyMap);	
		  conStr = conStr+"\"";
		   conStr = conStr.concat(":");
		   conStr = conStr+"\"";
		   conStr = conStr.concat(value);
		  conStr = conStr+"\"";
		   if (mapSize != 0)
		   {
			   conStr =  conStr.concat(","); 
		   } 
		}
		conStr = conStr.concat("}");		
		conStr = AES.encrypt(key, iv, conStr);
		conStr = conStr.replaceAll("\r|\n", "");
		return conStr;
	}
	
	public static String obtainkeyValue3776(String userName,String activeCode,String marketCode,String mobile,String mobileGUID,String mobileFormat,String remark) throws Exception
	{
		String key = "";
		String iv = "";
		
		Map<String,String> strMap  = new HashMap<String,String>();
		strMap.put("UserName", userName);
		strMap.put("ActivityCode", activeCode);
		strMap.put("MarketCode", marketCode);
		strMap.put("Mobile", mobile);
		strMap.put("MobileGUID", mobileGUID);
		strMap.put("MobileFormat", mobileFormat);
		strMap.put("Remark", remark);
		int mapSize = strMap.size();		
		String conStr  = "{";
		Iterator<String> keys = strMap.keySet().iterator(); 
		while(keys.hasNext()) {	
		   mapSize--;	
		   String keyMap = (String) keys.next(); 
		   String value=strMap.get(keyMap);
		   conStr = conStr+"\"";
		   conStr = conStr.concat(keyMap);	
		  conStr = conStr+"\"";
		   conStr = conStr.concat(":");
		   conStr = conStr+"\"";
		   conStr = conStr.concat(value);
		  conStr = conStr+"\"";
		   if (mapSize != 0)
		   {
			   conStr =  conStr.concat(","); 
		   } 
		}
		conStr = conStr.concat("}");		
		conStr = AES.encrypt(key, iv, conStr);
		conStr = conStr.replaceAll("\r|\n", "");
		return conStr;
	}
	
	
	public static String obtainRemark3775(String yztAcctId,String custName,String idCard)
	{
		Map<String,String> remarkMap  = new HashMap<String,String>();
		if (custName.length()!=0)
		{
			remarkMap.put("custName", custName);
		}		
		if (yztAcctId.length()!=0)
		{
			remarkMap.put("yztAcctId", yztAcctId);
		}
		if (idCard.length()!=0)
		{
			remarkMap.put("idCard", idCard);
		}		
		int mapSize = remarkMap.size();		
		String remarkStr  = "{";
		Iterator<String> keys = remarkMap.keySet().iterator(); 
		while(keys.hasNext()) {	
		   mapSize--;	
		   String keyMap = (String) keys.next(); 
		   String value=remarkMap.get(keyMap);
		   remarkStr = remarkStr+"\"";
		   remarkStr = remarkStr.concat(keyMap);	
		   remarkStr = remarkStr+"\"";
		  remarkStr = remarkStr.concat(":");
		   remarkStr = remarkStr+"\"";
		   remarkStr = remarkStr.concat(value);
		   remarkStr = remarkStr+"\"";
		   if (mapSize != 0)
		   {
			   remarkStr =  remarkStr.concat(","); 
		   } 
		}
		remarkStr = remarkStr.concat("}");
		remarkStr = remarkStr .replaceAll("\"", "\'");
		return remarkStr;
	}
	
	
	public static String showResult(String resp) throws Exception
	{
		return AES.decrypt("", "", resp);
	}

}
