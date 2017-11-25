package com.hpj.apicn.translate.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.hpj.apicn.app.AppKey;
import com.hpj.apicn.translate.Translate;
import com.hpj.apicn.util.HttpUtil;

/**
 * 谷歌翻译.<br><br>
 * 
 * <b>谷歌翻译返回的JSON串</b><br><br>
 * <b>
 * <pre>
 * {
 *   "data": {
 *     "translations": [
 *       {
 *         "translatedText": "提供高度可重用的静态实用方法"
 *       }
 *     ]
 *   }
 * }
 * </pre>
 * </b>
 * 
 * @author he_pe
 */
public class GoogleTranslate implements Translate{
	
	@Override
	public String api(String query) throws IOException {
		// 准备参数: https://cloud.google.com/translate/docs/reference/translate#body.QUERY_PARAMETERS
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("q", query));
		params.add(new BasicNameValuePair("source", "en"));
		params.add(new BasicNameValuePair("target", "zh-CN"));
		params.add(new BasicNameValuePair("key", AppKey.GOOGLE_KEY));

		return HttpUtil.sendPost(params, AppKey.GOOGLE_HOST);
	}
	
	@Override
	public String after(String apistr){
		// 解析过程参考结果示例
		Object data = JSON.parseObject(apistr).get("data");
		Object translations = JSON.parseObject(data.toString()).get("translations");
		Object arrFirst = JSON.parseArray(translations.toString()).get(0);
		Object result = JSON.parseObject(arrFirst.toString()).get("translatedText");
		return result.toString();
	}
	
}	
