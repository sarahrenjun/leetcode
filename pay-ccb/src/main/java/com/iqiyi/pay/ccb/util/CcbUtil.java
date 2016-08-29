package com.iqiyi.pay.ccb.util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 建行基础util类
 * @author renshuangjun_sx
 *
 */
public class CcbUtil {
	private static final Logger logger = LoggerFactory.getLogger(CcbUtil.class);

	/**
	 * 对请求参数进行转换
	 * @param paramMap原始请求参数
	 * @return
	 */
	public static Map<String, String> convertParamMap(
			Map<String, String[]> paramMap) {
	 Map<String, String> newParamMap = new HashMap<String, String>();
        for (Iterator iter = paramMap.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = paramMap.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            newParamMap.put(name, valueStr);
        }

        return newParamMap;
	}
	

	/**
	 * 生成签名
	 * 
	 * @param in   传输报文,用于签名的报文是不带"<txsign></txsign>"节点、并且去掉空格和换行后的xml报文
	 * @param key  私钥
	 * @return
	 */
	public static String getSign(byte[] in, PrivateKey key) {
		String asB64 = "";
		try {
			Signature signa = Signature.getInstance("MD5WithRSA");
			signa.initSign(key);
			signa.update(in);
			byte[] signaData = signa.sign();
			asB64 = Base64.getEncoder().encodeToString(signaData);
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
		} catch (InvalidKeyException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
		} catch (SignatureException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
		}

		return asB64;
	}
	
	/**
	 * 验证签名是否正确
	 * @param in 原报文
	 * @param signData 报文签名
	 * @param key 报文签名生成时使用的私钥对应的公钥
	 * @return
	 */
	public static boolean validSign(byte[] in,String signData,PublicKey key) {
		boolean flag = false;
		try {
			Signature signa = Signature.getInstance("MD5WithRSA");
			signa.initVerify(key);;
			signa.update(in);
			byte[] decData = Base64.getDecoder().decode(signData);
			flag = signa.verify(decData);
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
		} catch (InvalidKeyException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
		} catch (SignatureException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
		}
		return flag;
	}

	/**
	 * 私钥加载，把已有的私钥字符串加载为私钥类
	 * @param keyStr 私钥字符串
	 * @return
	 */
	public static PrivateKey getPrivateKey(String keyStr) {
		byte[] keyBytes = Base64.getDecoder().decode(keyStr);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		PrivateKey privateKey = null;
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			privateKey = keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
		} catch (InvalidKeySpecException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
		}

		return privateKey;
	}
	
	/**
	 * 密钥加载
	 * @param keyStr 密钥字符串，由银行提供
	 * @return
	 */
	public static SecretKey getSecretKey(String keyStr) {
		byte[] keyBytes = Base64.getDecoder().decode(keyStr);
		DESedeKeySpec keySpec;
		try {
			keySpec = new DESedeKeySpec(keyBytes);
			SecretKey secretKey = null;
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
			return keyFactory.generateSecret(keySpec);
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			return null;
		} catch (InvalidKeySpecException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			return null;
		} catch (InvalidKeyException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			return null;
		}
		
	}
	
	/**
	 * 对报文进行加密
	 * @param in 需要进行加密的字符组
	 * @param keyStr DESede密钥，由银行提供
	 * @return
	 */
	public static String encrypt(byte[] in , String keyStr) {
		try {
			SecretKey key = getSecretKey(keyStr);
			Cipher cipher = Cipher.getInstance("DESede");//创建密码器
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encData = cipher.doFinal(in);
			return Base64.getEncoder().encodeToString(encData);
		} catch (InvalidKeyException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			return null;
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			return null;
		} catch (NoSuchPaddingException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			return null;
		} catch (IllegalBlockSizeException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			return null;
		} catch (BadPaddingException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			return null;
		}
	}
	
	/**
	 * 对报文进行解密
	 * @param in 需要进行解密的报文字符组
	 * @param keyStr DESede密钥，由银行提供
	 * @return
	 */
	public static byte[] decrypt (byte[] in ,String keyStr) {
		try {
			SecretKey key = getSecretKey(keyStr);
			Cipher cipher = Cipher.getInstance("DESede");//创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decData = Base64.getDecoder().decode(in);
			return cipher.doFinal(decData);
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			return null;
		} catch (NoSuchPaddingException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			return null;
		} catch (InvalidKeyException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			return null;
		} catch (IllegalBlockSizeException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			return null;
		} catch (BadPaddingException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			return null;
		}
		
	}
	
	/**
	 * 向银行发送报文
	 * @param message 报文信息
	 * @return
	 */
	public static String sendMessage(String message,String urlString) {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(urlString);
		StringEntity entity = new StringEntity(message, 
				ContentType.create("plain/xml", Consts.UTF_8));
		httpPost.setEntity(entity);
		httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");
		try {
			HttpResponse response = client.execute(httpPost);
			return EntityUtils.toString(response.getEntity(),Consts.UTF_8);
		} catch (ClientProtocolException e) {
			e.getMessage();
			return null;
		} catch (IOException e) {
			e.getMessage();
			return null;
		}
	}
	
	/**
	 * 解析建行返回的交易数据
	 * @param xml 建行返回的报文数据
	 * @return
	 */
   public static Map<String, String> parseXml(String xml) {
        Map<String, String> map = new HashMap<String, String>();
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
