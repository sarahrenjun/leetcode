package com.iqiyi.pay.ccb.util;

import java.security.PrivateKey;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

/**
 * 报文生成类
 * @author renshuangjun_sx
 *
 */
@Component
public class CcbXmlBuilder {

	/**
	 * 生成报文
	 * 
	 * @param headerDataMap
	 *            报文头信息参数
	 * @param bodyDataMap
	 *            报文体信息参数
	 * @param keyStr
	 *            私钥字符串
	 * @return
	 */
	public String buildXML(Map<String, String> headerDataMap,Map<String, String> bodyDataMap, String keyStr) {
		Document doc = DocumentHelper.createDocument();
		Element root = DocumentHelper.createElement("Tran");
		Element header = this.buildHeaderXML(headerDataMap);
		Element body = this.buildBodyXML(bodyDataMap);
		try {
			root.add(body);
			root.add(header);
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			return null;
		}
		
		doc.setRootElement(root);
		byte[] message = doc.asXML().replaceAll("\\n", "").getBytes();
		PrivateKey privateKey = CcbUtil.getPrivateKey(keyStr);
		String sign = CcbUtil.getSign(message, privateKey);
		Element node = header.addElement("txsign");
		node.addText(sign);
		return doc.asXML().replaceAll("\\n", "");
	}

	/**
	 * 生成报文头信息
	 * 
	 * @param headerDataMap
	 *            报文头信息参数
	 * @return
	 */
	public Element buildHeaderXML(Map<String, String> headerDataMap) {
		Element root = DocumentHelper.createElement("Header");
		Element node;
		for (String key : headerDataMap.keySet()) {
			node = root.addElement(key);
			node.addText(headerDataMap.get(key));
		}
		return root;
	}

	/**
	 * 生成报文体信息
	 * 
	 * @param bodyDataMap
	 *            报文体信息参数
	 * @return
	 */
	public Element buildBodyXML(Map<String, String> bodyDataMap) {
		Element root = DocumentHelper.createElement("Body");
		Element subRoot = root.addElement("request");

		Element node;
		for (String key : bodyDataMap.keySet()) {
			node = subRoot.addElement(key);
			try {
				node.addText(bodyDataMap.get(key));
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
				System.out.println(e.getCause());
				return null;
			}
			
		}
		return root;
	}

}
