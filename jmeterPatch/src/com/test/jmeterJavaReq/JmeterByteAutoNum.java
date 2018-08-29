package com.test.jmeterJavaReq;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.xml.sax.SAXException;

import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

public class JmeterByteAutoNum extends AbstractJavaSamplerClient {

	
	private String username;
	private String uploadurl;
	private String pubkeyurl;
	private String contactnum;

	public Arguments getDefaultParameters() {

		Arguments params = new Arguments();
		params.addArgument("username", "");
		params.addArgument("uploadurl","");				
		params.addArgument("pubkeyurl","");				
		params.addArgument("contactnum","");
		return params;
	}

	public void setupTest() {
	}

	@Override
	public SampleResult runTest(JavaSamplerContext arg0) {

		username = arg0.getParameter("username");
		uploadurl = arg0.getParameter("uploadurl");
		pubkeyurl = arg0.getParameter("pubkeyurl");
		contactnum  = arg0.getParameter("contactnum"); 
		

		SampleResult sr= new SampleResult();
		sr.setSampleLabel("uploadCtz");
		sr.setDataEncoding("UTF-8");
		/*sr.setDataType("text");  
        sr.setContentType("text/plain"); */
		sr.setSuccessful(true);

		WebConversation webpost = new WebConversation();

		byte[] reqByte;
		PostMethodWebRequest post = null;
		try {
			reqByte = wrapUploadInfo(username, "qudaohao", pubkeyurl,
					wrapContactInput(contactnum));
			post = new PostMethodWebRequest(uploadurl,
					new ByteArrayInputStream(reqByte), "UTF-8");
		} catch (IOException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WebResponse postresponse = null;
		String resText = "";
		
		sr.sampleStart();
		try {
			postresponse = webpost.getResponse(post);
			resText = postresponse.getText();
		} catch (IOException | SAXException e) {
			 sr.setSuccessful(false);
		     sr.setResponseData(toStringStackTrace(e),"utf-8"); 
		}
		finally{
			sr.sampleEnd();
			webpost=null;
		}
		
		
		int resultCode = FastJsonUtils.getIntValueFromJsonResponse(resText,"result");
		if (resultCode != 0) {
			sr.setSuccessful(false);
		}
		//sr.setResponseCode(String.valueOf(resultCode));
		sr.setResponseData(resText,"utf-8");		
		return sr;
	}

	public void teardownTest(JavaSamplerContext arg0) {		
	}
	
	

	public static byte[] wrapUploadInfo(String username, String qudaohao,
			String pubkeyurl, Map<String, String> contactz_mobile_name)
			throws IOException, SAXException {

		int u = username.getBytes().length;
		int q = qudaohao.getBytes().length;

		int mobileLen = 0;
		int mobileNameLen = 0;

		for (Map.Entry<String, String> entry : contactz_mobile_name.entrySet()) {
			String mobile = entry.getKey();
			mobileLen += mobile.getBytes().length;
			mobileLen += 1;
			String mobileName = entry.getValue();
			mobileNameLen += mobileName.getBytes().length;
			mobileNameLen += 1;
		}
		int contactMoibleLen = mobileLen + mobileNameLen;
		int len = 1 + u + 1 + q + contactMoibleLen;

		ByteBuffer orionBf = ByteBuffer.allocate(len);

		orionBf.order(ByteOrder.LITTLE_ENDIAN);
		int offset = 0;

		orionBf.put((byte) u);
		offset++;
		orionBf.position(offset);

		orionBf.put(username.getBytes("UTF-8"));
		offset += u;
		orionBf.position(offset);

		orionBf.put((byte) q);
		offset++;
		orionBf.position(offset);

		orionBf.put(qudaohao.getBytes("UTF-8"));
		offset += q;
		orionBf.position(offset);

		for (Map.Entry<String, String> entry : contactz_mobile_name.entrySet()) {
			String mobile = entry.getKey();
			String mobileName = entry.getValue();

			byte[] mobileByte = mobile.getBytes("UTF-8");
			byte[] mobileNameByte = mobileName.getBytes("UTF-8");
			int mobileByteLen = mobileByte.length;
			int mobileNameByteLen = mobileNameByte.length;

			// 通讯录手机及名称
			orionBf.put((byte) mobileByteLen);
			offset++;
			orionBf.position(offset);

			orionBf.put(mobileByte);
			offset += mobileByteLen;
			orionBf.position(offset);

			orionBf.put((byte) mobileNameByteLen);
			offset++;
			orionBf.position(offset);

			orionBf.put(mobileNameByte);
			offset += mobileNameByteLen;
			orionBf.position(offset);

		}

		// 对原始内容 orionBf 进行 GZIP压缩
		byte[] gzipClearText = GZipUtils.compress(orionBf.array());
		int gzipClearTextLen = gzipClearText.length;
		byte[] gzipClearTextBwei = getBuweiAft(gzipClearText);

		// 通讯录AES密文
		String key = getRandomKey("key");
		String iv = getRandomIV("iv");
		byte[] ivBytes = iv.getBytes();
		int ivLen = ivBytes.length;
		// 通讯录aes加密
		byte aes[] = AesEncrypt.endecrypt(gzipClearTextBwei, key, iv, true);
		int aesLen = aes.length;

		String pubkey = getContactPubkey(pubkeyurl);
		// 针对key进行rsa加密
		RsaEncrypt RsaEncrypt = new RsaEncrypt();
		byte keyRsa[] = RsaEncrypt.RsaEncrypt(pubkey, key.getBytes());
		int keyRsaLen = keyRsa.length;

		// 组装后的数据
		int lencry = 4 + keyRsaLen + 4 + ivLen + aesLen;
		ByteBuffer aa = ByteBuffer.allocate(lencry);
		aa.order(ByteOrder.LITTLE_ENDIAN);
		int offsetWrap = 0;
		aa.putInt(keyRsaLen);
		offsetWrap += 4;
		aa.position(offsetWrap);

		aa.put(keyRsa);
		offsetWrap += keyRsaLen;
		aa.position(offsetWrap);

		aa.putInt(gzipClearTextLen);
		offsetWrap += 4;
		aa.position(offsetWrap);

		aa.put(ivBytes);
		offsetWrap += ivLen;
		aa.position(offsetWrap);

		aa.put(aes);
		offsetWrap += aesLen;
		aa.position(offsetWrap);

		return aa.array();

	}

	public static byte[] getBuweiAft(byte[] source) {
		int newlen = (source.length + 15) / 16 * 16;
		byte[] arrBTmp = source;
		byte[] arrB = new byte[newlen];

		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
			arrB[i] = arrBTmp[i];
		}
		return arrB;
	}

	public static String getRandomKey(String prefix) {
		SimpleDateFormat df = new SimpleDateFormat("ddHHmmss");
		Date date = new Date();
		return prefix + ((int) (Math.random() * (99999 - 10000 + 1)) + 10000)
				+ df.format(date);

	}

	public static String getRandomIV(String prefix) {
		SimpleDateFormat df = new SimpleDateFormat("ddHHmmss");
		Date date = new Date();
		return prefix
				+ ((int) (Math.random() * (999999 - 100000 + 1)) + 100000)
				+ df.format(date);
	}

	public static String getContactPubkey(String posturlString){
	
		
		String request = "{}";
		WebConversation webpost = new WebConversation();
		PostMethodWebRequest post = new PostMethodWebRequest(posturlString,
				new ByteArrayInputStream(request.getBytes()), "UTF-8");
		WebResponse postresponse = null;
		String pubkey = "";
		try {
			postresponse = webpost.getResponse(post);
			pubkey = FastJsonUtils.getValueFromJsonResponse(postresponse.getText(),
					"pubkey");
		} catch (IOException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			webpost = null;
			post = null;
			
		}
		return pubkey;
	}

	private static Map<String, String> wrapContactInput(String contactnum) {
		Map<String, String> txlMap = new HashMap<String, String>();
		int numUp = Integer.parseInt(contactnum);
		for (int num = 0; num < numUp; num++) {
			Long mobile = 15100001000L + num;
			String mapKey = String.valueOf(mobile);
			String mapVal = "mname" + num;
			txlMap.put(mapKey, mapVal);
		}
		return txlMap;
	}
	
	private String toStringStackTrace(Throwable e){  
	    String exception = null;  
	    try {  
	        StringWriter sw = new StringWriter();  
	        PrintWriter pw = new PrintWriter(sw);  
	        e.printStackTrace(pw);  
	        exception = sw.toString();  
	        pw.close();  
	        sw.close();  
	    } catch (Exception e1) {  
	        e1.printStackTrace();  
	    }  
	    return exception;  
	}

	
	/*public static void main(String[] args) { // TODO Auto-generated method
		Arguments params = new Arguments();
		params.addArgument("username", "王王哈3");
		params.addArgument("uploadurl",
				"http://*************:8080/Contacts/upload");
		params.addArgument("pubkeyurl",
				"http://**************:8080/Contacts/pubkey");
		JavaSamplerContext arg0 = new JavaSamplerContext(params);
		JmeterByteAutoNum test = new JmeterByteAutoNum();
		test.setupTest(arg0);
		test.runTest(arg0);
		test.teardownTest(arg0);
	}*/
	 
}
