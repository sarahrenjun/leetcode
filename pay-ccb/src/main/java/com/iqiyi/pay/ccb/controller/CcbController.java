package com.iqiyi.pay.ccb.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.iqiyi.pay.ccb.service.CcbBindService;
import com.iqiyi.pay.ccb.service.CcbInfoCheckService;
import com.iqiyi.pay.ccb.util.CcbParam;
import com.iqiyi.pay.ccb.util.CcbUtil;

/**
 * 
 * @author renshuangjun_sx
 *
 */
public class CcbController {
	@Autowired
	CcbBindService bindService;
	@Autowired
	CcbInfoCheckService infoCheckService;

	@RequestMapping(value = "/bind", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> bind(HttpServletRequest request) {
		Map<String, String[]> paramMap = request.getParameterMap();
	    Map<String, String> newParamMap = CcbUtil.convertParamMap(paramMap);
	    return bindService.service(newParamMap
	    		,CcbParam.PRIVATE_KEY,CcbParam.SECRET_KEY);
	}
	
	public Map<String, Object> infoCheck(HttpServletRequest request) {
		Map<String, String[]> paramMap = request.getParameterMap();
	    Map<String, String> newParamMap = CcbUtil.convertParamMap(paramMap);
	    return infoCheckService.service(newParamMap
	    		,CcbParam.PRIVATE_KEY,CcbParam.SECRET_KEY);
	}
}
