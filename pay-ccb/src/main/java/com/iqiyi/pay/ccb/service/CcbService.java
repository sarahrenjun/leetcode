package com.iqiyi.pay.ccb.service;

import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.iqiyi.pay.ccb.util.CcbParam;
import com.iqiyi.pay.ccb.util.CcbUtil;
import com.iqiyi.pay.ccb.util.CcbXmlBuilder;

/**
 * 银行卡操作基础服务
 * @author renshuangjun_sx
 *
 */
public class CcbService {
	private static final Logger logger = LoggerFactory
			.getLogger(CcbService.class);
	
	private CcbXmlBuilder xmlBuilder = new CcbXmlBuilder();
	
	/**
	 * 银行卡操作基础服务
	 * @param urlString 请求的url地址
	 * @param params 请求参数
	 * @param privateKey 签名生成私钥，由商户生成，对应的公钥需要提供给银行
	 * @param secretKey 报文加密密钥，由银行提供
	 * @return
	 */
	public Map<String,Object> service(String urlString, Map<String,String> params,String privateKey,String secretKey) {
		Map<String,String> headerConfig = getHeaderConfig(params);
		Map<String,String> bodyConfig = getBodyConfig(params);
		String xml= xmlBuilder.buildXML(headerConfig, bodyConfig, privateKey);
		if(xml == null) {
			return null;
		}
		String message = CcbUtil.encrypt(xml.getBytes(), secretKey);
		String receive = CcbUtil.sendMessage(message, urlString);
		return getResult(receive);
	}
	
	/**
	 * 配置报文头信息请求参数
	 * 
	 * @param params
	 *            请求参数
	 * @return
	 */
	protected Map<String, String> getHeaderConfig(Map<String, String> params) {
		Map<String, String> dataMap = new HashMap<String, String>();
		String tradeCode = params.get(CcbParam.TRADE_CODE);// 交易码
		String sysSequence = params.get(CcbParam.SYS_SEQUENCE);// 系统序列号
		String tradeData = new Date(System.currentTimeMillis()).toString()
				.replaceAll("-", "");// 交易日期
		String tradeTime = new Time(System.currentTimeMillis()).toString()
				.replaceAll(":", "");// 交易时间
		String terminalInfo = params.get(CcbParam.TERMINAL_INFO);
		dataMap.put(CcbParam.TRADE_CODE, tradeCode);
		dataMap.put(CcbParam.SYS_SEQUENCE, sysSequence);
		dataMap.put(CcbParam.TRADE_DATA, tradeData);
		dataMap.put(CcbParam.TRADE_TIME, tradeTime);
		dataMap.put(CcbParam.TERMINAL_INFO, terminalInfo);
		return dataMap;
	}
	
	/**
	 * 配置报文体信息参数
	 * @param paramsMap请求参数
	 * @return
	 */
	protected Map<String,String> getBodyConfig(Map<String,String> paramMap) {
		return paramMap;
	}
	
	/**
	 * 分析请求结果
	 * @param xml请求返回报文
	 * @return
	 */
	protected Map<String,Object> getResult(String xml) {
		return new HashMap<String,Object>();
	}
	
	
}
