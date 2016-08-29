package com.iqiyi.pay.ccb.service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TestCcbInfoCheckService {
	
	private CcbInfoCheckService cics = new CcbInfoCheckService();
	
	String keyStr="HK6Jy6OrfB7ZkZg6WD/rQIZfNeFn+nrrXQCTfN7Cv3cAADoAAAAAAOPCv3cAAAAAAM35CFzU/wsIAAAAVNT/C8LWWgwUwXwDkMB8A+GrWgzEwXwDlFzAd3AgvncAAAAAZAYAAK8nVQwUwXwDZAAAAEAAAABn0/8LXNT/C9PAWgwUwXwDQAAAAATBfAMJTRN4SLuBCnzBfAMAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAABEWnGJorzX8xAuTW1+kKO3zOL5ESpEX3uYttX1BhgrP1RqgZmyzOcDID5dfY6gs8fc8gkhOlRvi6jG5QUWKDtPO7f8CyC1YQxQvmEMAAAAAASCwBckn0F6gAAAAGjT/ws=";  
	
	@Test
	public void test() {
		 PrivateKey privatekey= null;;
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
			keyPairGen.initialize(1024);
			KeyPair keyPair = keyPairGen.generateKeyPair();
			privatekey = keyPair.getPrivate();
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
		}
		byte[] privateData = privatekey.getEncoded();
		String key = Base64.getEncoder().encodeToString(privateData);
		String secretKey = keyStr;
		Map<String,String> params = new HashMap<String,String>();
		params.put("txcode", "0");
		params.put("txseq", "2345");
		params.put("tminf", "123465476589709");
		params.put("tx_flag", "0");
		//params.put("cert_id", "123456");
		params.put("cert_typ", "1");
		params.put("cust_nm", "sarah");
		params.put("acct_no", "123354541352");
		params.put("mobile", "1234567890");
		Map<String,Object> result = cics.service(params, key, secretKey);
		System.out.println(result);
	}

}
