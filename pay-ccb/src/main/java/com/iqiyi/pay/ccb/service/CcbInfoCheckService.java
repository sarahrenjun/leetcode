package com.iqiyi.pay.ccb.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.iqiyi.pay.ccb.util.CcbParam;

/**
 * 客户信息校验
 * @author renshuangjun_sx
 *
 */
@Component
public class CcbInfoCheckService extends CcbService{
	private static final Logger logger = LoggerFactory
			.getLogger(CcbBindService.class);

	public Map<String, Object> service(Map<String, String> params
			,String privateKey,String secretKey) {
		return super.service(CcbParam.CCB_CHECK_URL, params, privateKey, secretKey);

	}

	

	/**
	 * 配置签约头信息请求参数
	 * 
	 * @param params
	 *            请求参数
	 * @return
	 */
	public Map<String, String> getHeaderConfig(Map<String, String> params) {
		return super.getHeaderConfig(params);
	}

	/**
	 * 配置客户信息校验报文体信息参数
	 * 
	 * @param params请求参数
	 * @return
	 */
	public Map<String, String> getBodyConfig(Map<String, String> params) {
		Map<String, String> dataMap = new HashMap<String, String>();
		String tradeFlag = params.get(CcbParam.TRADE_FLAG);// 交易标志 0签约 1注销 2支付
		String certificateId = params.get(CcbParam.CERTIFICATE_ID);// 证件号
		String certificateType = params.get(CcbParam.CERTIFICATE_TYPE);// 证件类型
		String customName = params.get(CcbParam.CUSTOM_NAME);// 客户姓名
		String customAccount = params.get(CcbParam.CUSTOM_ACCOUNT);// 客户账号
		String customMobile = params.get(CcbParam.CUSTOM_MOBILE);// 客户手机号
		dataMap.put(CcbParam.SERVICE_CODE, CcbParam.SERVICE_CODE_IQIYI);// 商户代码
		dataMap.put(CcbParam.COUNTER_CODE, CcbParam.COUNTER_CODE_IQIYI);// 柜台号
		if(tradeFlag == "0") {
			dataMap.put(CcbParam.CERTIFICATE_ID, certificateId);
			dataMap.put(CcbParam.CUSTOM_NAME, customName);
		} 
		if(certificateType != null) {
			dataMap.put(CcbParam.CERTIFICATE_TYPE, certificateType);
		}
		dataMap.put(CcbParam.CUSTOM_ACCOUNT, customAccount);
		dataMap.put(CcbParam.CUSTOM_MOBILE, customMobile);
	
		return dataMap;
	}

	/**
	 * 对返回结果进行分析
	 *@param xml 返回报文内容
	 *@return
	 */
	public Map<String,Object> getResult(String xml) {

		 Map<String, Object> map = new HashMap<String, Object>();
	        try {
	            Document doc = DocumentHelper.parseText(xml);
	            Element rootElement = doc.getRootElement();
	            Element header = rootElement.element("Header");
	            List<Element> headerElements = header.elements();
	            for (Element element : headerElements) {
	                map.put(element.getName(), element.getTextTrim());
	            }
	            Element body = rootElement.element("Body");
	            Element response = body.element("response");
	            List<Element> responseElements  = response.elements();
	            for (Element element : responseElements) {
	            	map.put(element.getName(), element.getTextTrim());
	            }
	            return map;
	        } catch (Exception e) {
	            logger.error("[module:WechatAppV3DutPayment] [method:parseXml] [xml:{}] [error:{}]",
	                    new Object[]{xml, e.getMessage()}, e);
	        }
	        return null;	
        }
}
