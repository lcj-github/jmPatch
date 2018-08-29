
package com.test.shell;


import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.thoughtworks.xstream.core.util.Base64Encoder;

/*
 * AES加解密  
 */

public class AES {
	public static final int strLength = 16; //加密key与向量长度为16�?
	
	
    /**
     * 解密
     * @param key 加密KEY
     * @param iv 加密向量
     * @param encryptStr 加密字符�?
     * @return 解密结果
     * @throws Exception
     */
    public static String decrypt(String key, String iv, String encryptStr) throws Exception{
    	String replacedStr = Base64Restore(encryptStr); //替换特殊字符
    	String pkey;
    	String piv;
		pkey = PadRight(key);
		piv = PadRight(iv);
    	return Decrypt(replacedStr, pkey, piv);
    }
    
    
    /**
     * 加密
     * @param key 加密KEY
     * @param iv 加密向量
     * @param str 加密字符�?
     * @return 加密结果
     * @throws Exception
     */
    public static String encrypt(String key, String iv, String str) throws Exception{
    	String pkey ;
    	String piv ;
		pkey = PadRight(key);
		piv = PadRight(iv);
    	String encryStr =  Encrypt(str, pkey, piv);
    	if(encryStr!=null){
    		return Base64Replace(encryStr);
    	}
    	return null;
    }
    

	private static String Encrypt(String sSrc, String sKey,String siv) throws Exception {
		
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否�?6�?
        if (sKey.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        // 判断加密向量16�?
        if(siv==null){
        	System.out.print("加密向量iv为空null");
        	return null;
        }
        byte[] dataBytes = sSrc.getBytes("utf-8");
       
        SecretKeySpec keyspec = new SecretKeySpec(sKey.getBytes("utf-8"), "AES");
       
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");//"算法/模式/补码方式"
        int plaintextLength = dataBytes.length;
        int blockSize = cipher.getBlockSize();
        if (plaintextLength % blockSize != 0) {
            plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
        }

        byte[] plaintext = new byte[plaintextLength];
        System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
        
        IvParameterSpec iv = new IvParameterSpec(siv.getBytes("utf-8"));//使用CBC模式，需要一个向量iv，可增加加密算法的强�?
        cipher.init(Cipher.ENCRYPT_MODE, keyspec, iv);
        byte[] encrypted = cipher.doFinal(plaintext);

        Base64Encoder encoder = new Base64Encoder();
		return encoder.encode(encrypted);//使用BASE64做转码功能，同时能起�?次加密的作用�?
    }
	
	
	private static String Decrypt(String sSrc, String sKey,String siv) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否�?6�?
            if (sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            // 判断加密向量
            if(siv==null){
            	System.out.print("加密向量iv为空null");
            	return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            IvParameterSpec iv = new IvParameterSpec(siv
                    .getBytes("utf-8"));
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            
            Base64Encoder encoder = new Base64Encoder();
            byte[] encrypted1 = encoder.decode(sSrc);//先用base64解密
    		
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, "UTF-8");
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }
    
    /**
     * 对字符串不满足指定长度的在右侧补空格
     * @param targetStr 
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String PadRight(String targetStr) throws UnsupportedEncodingException{
    	if (targetStr==null) {
			return null;
		}
        int curLength = targetStr.getBytes().length;
        if(targetStr!=null && curLength>strLength)
             targetStr = SubStringByte(targetStr);
         String newString = "";
        int cutLength = strLength-targetStr.getBytes().length;
        for(int i=0;i<cutLength;i++)
             newString +=" ";
        String preString = targetStr+newString;
        return preString;
    }
    
    public static String SubStringByte(String targetStr){
        while(targetStr.getBytes().length>strLength)
             targetStr = targetStr.substring(0,targetStr.length()-1);
        return targetStr;
    }
    
    /**
     * 从普通字符串转换为�?用于URL的Base64编码字符�?
     * @param normalString
     * @return
     */
    public static String Base64Replace(String normalString){
    	return normalString.replace('+', '*').replace('/', '-').replace('=', '.');
    }
    
    /**
     * 从普通字符串转换为�?用于URL的Base64编码字符�?
     * @param base64String
     * @return
     */
    public static String Base64Restore(String base64String)
    {
        return base64String.replace('.', '=').replace('*', '+').replace('-', '/');
    }
    
    
	public static void main(String[] args) throws Exception {
		
		String key = "*.*.com.cn";
		String iv = "*.*.com.cn";
		String jiamiResult = encrypt(key, iv, "320925198112111122");		
		System.out.println("身份证号为：=" + jiamiResult);
		String jiemiResult = decrypt(key, iv, jiamiResult);
		System.out.println("解完密后的身份证号为：=" + jiemiResult);
		
		
		
		
		/*String key = "*.*.com.cn";
		String iv = "*.*.com.cn";
		
		Map<String,String> strMap  = new HashMap<String,String>();
		strMap.put("UserName", "1001");
		strMap.put("ActivityCode", "51");
		strMap.put("MarketCode", "A31784");
		strMap.put("Mobile", "0ec26778e235f172e18be2e5b74822c4");
		strMap.put("MobileGUID", "");
		strMap.put("MobileFormat", "1391****658");
		strMap.put("Remark", "");
		
		int mapSize = strMap.size();
		
		System.out.println("mapSize = "+mapSize);
		
		
		String conStr  = "{";
		Iterator<String> keys = strMap.keySet().iterator(); 

		while(keys.hasNext()) {	
		   mapSize--;
		   
		   
		   String key = (String) keys.next(); 
		   String value=strMap.get(key);
		   
		   conStr = conStr+"\"";
		   conStr = conStr.concat(key);	
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
		
		System.out.println("conStr==== = "+conStr);*/
	}
    
}
