package com.iqiyi.pay.ccb.service;


import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.iqiyi.pay.ccb.util.CcbParam;


/**
 * 银行卡绑定服务
 * @author renshuangjun_sx
 *
 */
@Component
public class CcbBindService extends CcbService{
	private static final Logger logger = LoggerFactory
			.getLogger(CcbBindService.class);

	/**
	 * 银行卡绑定服务
	 * @param params 请求参数
	 * @param privateKey 签名生成私钥
	 * @param secretKey 报文加密密钥
	 * @return
	 */
	public Map<String, Object> service(Map<String, String> params
			,String privateKey,String secretKey) {
		return super.service(CcbParam.CCB_BIND_URL, params, privateKey, secretKey);

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
	 * 银行卡绑定短信验证报文体信息参数
	 * 
	 * @param params请求参数
	 * @return
	 */
	public Map<String,String> getBodyConfig (Map<String, String> params) {
		Map<String, String> dataMap = new HashMap<String, String>();
		String tradeFlag = params.get(CcbParam.TRADE_FLAG);// 交易标志 0签约 1注销
		String certificateId = params.get(CcbParam.CERTIFICATE_ID);// 证件号
		String certificateType = params.get(CcbParam.CERTIFICATE_TYPE);// 证件类型
		String customName = params.get(CcbParam.CUSTOM_NAME);// 客户姓名
		String customAccount = params.get(CcbParam.CUSTOM_ACCOUNT);// 客户账号
		String customMobile = params.get(CcbParam.CUSTOM_MOBILE);// 客户手机号
		String shortMessage = params.get(CcbParam.SHORT_MESSAGE);//短信验证码
		dataMap.put(CcbParam.TRADE_FLAG, tradeFlag);
		dataMap.put(CcbParam.SERVICE_CODE, CcbParam.SERVICE_CODE_IQIYI);// 商户代码
		dataMap.put(CcbParam.COUNTER_CODE, CcbParam.COUNTER_CODE_IQIYI);// 柜台号
		if(tradeFlag == "0") {
			dataMap.put(CcbParam.CERTIFICATE_ID, certificateId);
			dataMap.put(CcbParam.CUSTOM_NAME, customName);
			dataMap.put(CcbParam.CUSTOM_MOBILE, customMobile);
		}
		if(certificateType != null) {
			dataMap.put(CcbParam.CERTIFICATE_TYPE, certificateType);
		}
		dataMap.put(CcbParam.CUSTOM_ACCOUNT, customAccount);
		if(shortMessage != null) {
			dataMap.put(CcbParam.SHORT_MESSAGE, shortMessage);
		}
		return dataMap;
	}

}
