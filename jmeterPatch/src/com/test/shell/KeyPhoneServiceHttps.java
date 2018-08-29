package com.test.shell;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



public class KeyPhoneServiceHttps {
	
	public static String obtainPara(Integer callType,String valFrom,String mktcdFrom,String valGo,String mktcdGo) throws Exception
	{
		String key = "";
		String iv = "";
		Map<String,Object> remarkMap  = new HashMap<String,Object>();
		if (callType != null)
		{
			remarkMap.put("callType", callType);
		}		
		if (valFrom.length()!=0)
		{
			remarkMap.put("valFrom", valFrom);
		}		
		if (mktcdFrom.length()!=0)
		{
			remarkMap.put("mktcdFrom", mktcdFrom);
		}
		if (valGo.length()!=0)
		{
			remarkMap.put("valGo", valGo);
		}
		if (mktcdGo.length()!=0)
		{
			remarkMap.put("mktcdGo", mktcdGo);
		}
		
		int mapSize = remarkMap.size();		
		String remarkStr  = "{";
		Iterator<String> keys = remarkMap.keySet().iterator(); 
		while(keys.hasNext()) {	
		   mapSize--;	
		   String keyMap = (String) keys.next(); 
		   Object value=remarkMap.get(keyMap);
		   
		   if (value instanceof String)
		   {			   
			   String val = (String) value;			   
			   remarkStr = remarkStr+"\"";
			   remarkStr = remarkStr.concat(keyMap);	
			   remarkStr = remarkStr+"\"";
			   remarkStr = remarkStr.concat(":");
			   remarkStr = remarkStr+"\"";
			   remarkStr = remarkStr.concat(val);
			   remarkStr = remarkStr+"\""; 
		   }
		   else if (value instanceof Integer) {
			   String val = String.valueOf(value) ;
			   remarkStr = remarkStr+"\"";
			   remarkStr = remarkStr.concat(keyMap);	
			   remarkStr = remarkStr+"\"";
			   remarkStr = remarkStr.concat(":");
			   remarkStr = remarkStr.concat(val);
		 } 
		   
		   if (mapSize != 0)
		   {
			   remarkStr =  remarkStr.concat(","); 
		   } 
		   
		}
		remarkStr = remarkStr.concat("}");		
		remarkStr = remarkStr .replaceAll("\"", "\'");
		remarkStr = AES.encrypt(key, iv, remarkStr);
		remarkStr = remarkStr.replaceAll("\r|\n", "");
		return remarkStr;
	}
	
	
	public static String showResult(String resp) throws Exception
	{
		return AES.decrypt("", "", resp);
	}
	
	/*public static void main(String[] args) throws Exception
	{
		String para  = obtainPara( 1,"1862130****", "1200", "185****7119","1200");
		
		System.out.println("para==="+para);
	}*/

}
