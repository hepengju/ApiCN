package com.hpj.translate.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.hpj.translate.AppKey;
import com.hpj.translate.Translate;
import com.hpj.translate.util.HttpUtil;

/**
 * 百度翻译.<br><br>
 * 
 * <b>API返回结果示例 </b><br><br>
 * <b>
 * <pre>
 * {
 *   "from": "en",
 *   "to": "zh",
 *   "trans_result": [{
 *      "src": "Provides highly reusable static utility methods",
 *      "dst": "\u63d0\u4f9b\u9ad8\u5ea6\u53ef\u91cd\u7528\u7684\u9759\u6001\u5b9e\u7528\u65b9\u6cd5\u3002"
 *   }]
 * }
 * </pre>
 * </b>
 * 
 * @author he_pe
 */
public class BaiduTranslate implements Translate{
	
	@Override
	public String api(String query) throws IOException {
		// 准备参数
		String salt = RandomStringUtils.randomNumeric(8);
		String sign = DigestUtils.md5Hex(AppKey.BAIDU_APP + query + salt + AppKey.BAIDU_KEY);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("q", query));
		params.add(new BasicNameValuePair("from", "en"));
		params.add(new BasicNameValuePair("to", "zh"));
		params.add(new BasicNameValuePair("appid", AppKey.BAIDU_APP));
		params.add(new BasicNameValuePair("salt", salt));
		params.add(new BasicNameValuePair("sign", sign));

		return HttpUtil.sendPost(params, AppKey.BAIDU_HOST);
	}

	@Override
	public String after(String apistr){
		// 解析过程参考结果示例
		Object transResult = JSON.parseObject(apistr).get("trans_result");
		Object arrFirst = JSON.parseArray(transResult.toString()).get(0);
		Object result = JSON.parseObject(arrFirst.toString()).get("dst");
		return result.toString();
	}
	
}	

