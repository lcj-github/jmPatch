package com.test.jmeterJavaReq;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesEncrypt {


	public static SecretKeySpec getKey(String strKey) {
		byte[] arrBTmp = strKey.getBytes();
		byte[] arrB = new byte[16]; // 创建�?个空�?16位字节数组（默认值为0�?

		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
			arrB[i] = arrBTmp[i];
		}

		SecretKeySpec skeySpec = new SecretKeySpec(arrB, "AES");

		return skeySpec;
	}

	public static byte[] getIv(String strKey) {
		byte[] arrBTmp = strKey.getBytes();
		byte[] arrB = new byte[16]; // 创建�?个空�?16位字节数组（默认值为0�?

		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
			arrB[i] = arrBTmp[i];
		}

		return arrB;
	}

	public static byte[] getVals(String source) {
		int newlen = (source.length() + 15) / 16 * 16;		
		byte[] arrBTmp = source.getBytes();
		byte[] arrB = new byte[newlen]; // 创建�?个空�?16位字节数组（默认值为0�?

		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
			arrB[i] = arrBTmp[i];
		}
		System.out.println("arrBTmp长度�?"+arrBTmp.length);

		return arrB;
	}

	public static byte[] endecrypt(byte[] bs,String rkey, String riv,boolean encrypt) {
		SecretKeySpec skeySpec;
		byte[] ivStr;
		try {
			skeySpec = getKey(rkey);
			ivStr = getIv(riv);
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");

			IvParameterSpec iv = new IvParameterSpec(ivStr);
			if (encrypt) {
				cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			} else {
				cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			}
			byte[] encrypted1 = bs;
			byte[] original = cipher.doFinal(encrypted1);
			return original;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] dummy = new byte[1];
		return dummy;
	}

	public static byte[] aes_encrypt(String source,String rkey,String riv) {
		return endecrypt(getVals(source),rkey,riv, true);
	}

	public static String aes_decrypt(byte[] source,String rkey,String riv) {
		byte[] outbytes = endecrypt(source, rkey, riv,false);
		int reallen = 0;
		for (int i = 0; i < outbytes.length; i++) {
			if (outbytes[i] == 0) {
				break;
			}
			reallen++;
		}
		byte[] newbytes = new byte[reallen];
		for (int i = 0; i < reallen; i++) {
			newbytes[i] = outbytes[i];
		}
		return new String(newbytes);
	}

}
