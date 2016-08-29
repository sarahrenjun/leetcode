package com.iqiyi.pay.ccb.util;

import static org.junit.Assert.*;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.iqiyi.pay.ccb.service.CcbBindService;

public class TestCcbXmlBuilder {

	@Test
	public void testBuildHeaderXml() {
		Map<String,String> map = new HashMap<String,String>();
		map.put("txcode", "123456");
		map.put("txseq", "2345");
		map.put("tminf", "123465476589709");
		CcbBindService cbs = new CcbBindService();
		CcbXmlBuilder cxb = new CcbXmlBuilder();
		Map<String,String> headerDataMap = cbs.getHeaderConfig(map);
		System.out.println(cxb.buildHeaderXML(headerDataMap));
	}
	
	@Test
	public void testBuildBodyXml() {
		Map<String,String> mapBody = new HashMap<String,String>();
		mapBody.put("tx_flag", "0");
		mapBody.put("cert_id", "123456");
		mapBody.put("cert_typ", "1");
		mapBody.put("cust_nm", "sarah");
		mapBody.put("acct_no", "123354541352");
		mapBody.put("mobile", "1234567890");
		CcbBindService cbs = new CcbBindService();
		CcbXmlBuilder cxb = new CcbXmlBuilder();
		Map<String,String> bodyDataMap = cbs.getBodyConfig(mapBody);
		System.out.println(cxb.buildBodyXML(bodyDataMap));
	}
	
	@Test
	public void testBuildXml() {
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
		Map<String,String> map = new HashMap<String,String>();
		map.put("txcode", "123456");
		map.put("txseq", "2345");
		map.put("tminf", "123465476589709");
		CcbBindService cbs = new CcbBindService();
		CcbXmlBuilder cxb = new CcbXmlBuilder();
		Map<String,String> headerDataMap = cbs.getHeaderConfig(map);
		Map<String,String> mapBody = new HashMap<String,String>();
		mapBody.put("tx_flag", "0");
		mapBody.put("cert_id", "123456");
		mapBody.put("cert_typ", "1");
		mapBody.put("cust_nm", "sarah");
		mapBody.put("acct_no", "123354541352");
		mapBody.put("mobile", "1234567890");
		Map<String,String> bodyDataMap = cbs.getBodyConfig(mapBody);
		System.out.println(cxb.buildXML(headerDataMap, bodyDataMap, keyStr));
	}

}
