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
 * 有道翻译.<br><br>
 * 
 * <b>有道翻译API返回结果示例 </b><br><br>
 * <b>
 * <pre>
 * {
 *   "translation": ["提供高度可重用的静态实用方法"],
 *   "errorCode": "0",
 *   "dict": {
 *       "url": "yddict://m.youdao.com/dict?le=eng&q=Provides+highly+reusable+static+utility+methods"
 *   },
 *   "webdict": {
 *       "url": "http://m.youdao.com/dict?le=eng&q=Provides+highly+reusable+static+utility+methods"
 *   },
 *   "l": "EN2zh-CHS"
 * }
 * </pre>
 * </b>
 * 
 * @author he_pe
 */
public class YoudaoTranslate implements Translate{

	@Override
	public String api(String query) throws IOException {
		// 准备参数
		String salt = RandomStringUtils.randomNumeric(8);
		String sign = DigestUtils.md5Hex(AppKey.YOUDAO_APP + query + salt + AppKey.YOUDAO_KEY);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("q", query));
		params.add(new BasicNameValuePair("from", "EN"));
		params.add(new BasicNameValuePair("to", "zh-CHS"));
		params.add(new BasicNameValuePair("appKey", AppKey.YOUDAO_APP));
		params.add(new BasicNameValuePair("salt", salt));
		params.add(new BasicNameValuePair("sign", sign));

		return HttpUtil.sendPost(params, AppKey.YOUDAO_HOST);
	}
	
	@Override
	public String after(String apistr){
		// 解析过程参考结果示例
		String translation = JSON.parseObject(apistr).get("translation").toString();
		return translation.substring(2, translation.length() - 2);
	}
}