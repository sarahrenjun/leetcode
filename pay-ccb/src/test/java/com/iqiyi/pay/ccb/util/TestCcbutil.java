package com.iqiyi.pay.ccb.util;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.junit.Test;


public class TestCcbutil{
	
	CcbUtil cu = new CcbUtil();
	
	String keyStr="HK6Jy6OrfB7ZkZg6WD/rQIZfNeFn+nrrXQCTfN7Cv3cAADoAAAAAAOPCv3cAAAAAAM35CFzU/wsIAAAAVNT/C8LWWgwUwXwDkMB8A+GrWgzEwXwDlFzAd3AgvncAAAAAZAYAAK8nVQwUwXwDZAAAAEAAAABn0/8LXNT/C9PAWgwUwXwDQAAAAATBfAMJTRN4SLuBCnzBfAMAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAABEWnGJorzX8xAuTW1+kKO3zOL5ESpEX3uYttX1BhgrP1RqgZmyzOcDID5dfY6gs8fc8gkhOlRvi6jG5QUWKDtPO7f8CyC1YQxQvmEMAAAAAASCwBckn0F6gAAAAGjT/ws=";  
	
	@Test
	public void TestGetPrivateKey() {
	   PrivateKey privateKey = null;;
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
			keyPairGen.initialize(1024);
			KeyPair keyPair = keyPairGen.generateKeyPair();
			privateKey = keyPair.getPrivate();
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
		}
		byte[] privateData = privateKey.getEncoded();
		String keyStr = Base64.getEncoder().encodeToString(privateData);
		System.out.println(keyStr);
		cu.getPrivateKey("keyStr");
	}
	
	@Test
	public void TestSendMessage() {
		System.out.println(cu.sendMessage("aaa",CcbParam.CCB_BIND_URL));
	}
	
	@Test
	public void TestEncryptAndDncrypt() throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
		//SecretKeyFactory skf = SecretKeyFactory.getInstance("DESede");
		//SecretKey key = skf.generateSecret(new DESedeKeySpec(Base64.getDecoder().decode(keyStr.getBytes())));
		byte[] in = new String("aaa").getBytes();
		String encData = cu.encrypt(in, keyStr);
		byte[] encBytes = encData.getBytes();
		byte[] decBytes = cu.decrypt(encBytes, keyStr);
		String decData = new String(decBytes);
		System.out.println(decData);
		
	}
	
	@Test
	public void TestSignAndValid() {
		PrivateKey privateKey = null;
		PublicKey publicKey = null;
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
			keyPairGen.initialize(1024);
			KeyPair keyPair = keyPairGen.generateKeyPair();
			privateKey = keyPair.getPrivate();
			publicKey = keyPair.getPublic();
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
		}
		
		String testStr = "test";
		byte[] in = testStr.getBytes();
		String sign = cu.getSign(in, privateKey);
		System.out.println(sign);
		boolean flag = cu.validSign(in, sign, publicKey);
		System.out.println(flag);
	}
	
	@Test
	public void TestParseXml() {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Tran><Header><txseq>2345</txseq><txtime>180319</txtime><txcode>123456</txcode><tminf>123465476589709</tminf><txdate>20160714</txdate><txsign>krRM8PcmZWfAKNIC9DXbIzUL+wwsAdPBr/Quxfy7bUJ/zmHfQqSnCkbsrZPSn4S83fOFhGf1ZSz4ueIT1H7h5EqDSOe+VfoaFJgZYgStqS9J1al+V4fBP8yeNOqmvL3X4HWYl51HJqYvJJdK5BZhcdEsy8X+HAzmGrkhKGZSFjU=</txsign></Header><Body><response><cert_typ>1</cert_typ><cust_nm>sarah</cust_nm><tx_flag>0</tx_flag><acct_no>123354541352</acct_no><mobile>1234567890</mobile><cert_id>123456</cert_id><cunt_no>iqiyi</cunt_no><shop_no>iqiyi</shop_no></response></Body></Tran>";
		Map<String,String> params = cu.parseXml(xml);
		System.out.println(params);
	}
	
	
}
