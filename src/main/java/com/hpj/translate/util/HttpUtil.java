package com.hpj.translate.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * 发送Http请求工具
 * 
 * @author he_pe
 */
public class HttpUtil {

	// 客户端: 一个即可
	private static final CloseableHttpClient client = HttpClients.createDefault();
	
	/**
	 * 发送post请求.<br>
	 * 多个API接口的Http请求都一样,代码抽取出来.
	 */
	public static String sendPost(List<NameValuePair> params,String host)
			throws UnsupportedEncodingException, IOException, ClientProtocolException {
		// 设置URL和参数
		HttpPost httpPost = null;
		CloseableHttpResponse response = null;
		try {
			httpPost = new HttpPost(host);
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			httpPost.setHeader("User-Agent", "Apache-HttpClient/4.5.2 (Java/1.8.0_91)");
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			// 发送Post请求,得到返回结果 
			response = client.execute(httpPost);
			return EntityUtils.toString(response.getEntity());
		} finally {
			if(response != null) response.close();
			if(httpPost != null) httpPost.releaseConnection();
		}
	}
}
