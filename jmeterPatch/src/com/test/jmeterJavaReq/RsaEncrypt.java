package com.test.jmeterJavaReq;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.bouncycastle.util.encoders.Base64;
  
public class RsaEncrypt{

      
     public static final String DEFAULT_PUBLIC_KEY=   
    		 "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDZQnOdHjIWNWtI6xmQ3/28I08y" + "\r" +  
    			        "VDKiYKzgeGct9jAxvhDJJwd965JaWFm5TFz2/dkfkO9EF/19VTtDxH1/mpqmcZvx" + "\r" +  
    			        "gYEujkRzEop3zcwlDqJ0hmhDfu+W9N6d2tABXgpv528fxnpIFiuXZClnGaT9oAyf" + "\r" +  
    			        "2DcQacbToT8OJcAntQIDAQAB" + "\r"; 
      

 
    /** 
     * 加密过程 
     * @param publicKey 公钥 
     * @param plainTextData 明文数据 
     * @return 
     * @throws IOException 
     * @throws Exception 加密过程中的异常信息 
     */  
    public byte[] RsaEncrypt(String rawPubKey, byte[] rawData) throws IOException {
        PublicKey mPubKey = null;
        try {
            String publicKeyPEM = rawPubKey.replace("-----BEGIN PUBLIC KEY-----\n", "");
            publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            mPubKey = keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decode(publicKeyPEM)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] encryptedData = null;
        ByteArrayOutputStream out = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, mPubKey);
            out = new ByteArrayOutputStream();
            int inputLen = rawData.length;
            int offSet = 0;
            byte[] cache = null;
            int MAX_ENCRYPT_BLOCK = 117;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(rawData, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(rawData, offSet, inputLen - offSet);
                    offSet += inputLen - offSet;
                }
                out.write(cache, 0, cache.length);
                offSet += MAX_ENCRYPT_BLOCK;
            }
            encryptedData = out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
          out.close();
        }
        return encryptedData;
    }
   
}  