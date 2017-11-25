package com.hpj.apicn.translate;


import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hpj.apicn.app.AppConst;

/**
 * 翻译接口.
 * 
 * <p>处理流程如下: <p> 
 * <p>
 * <table border="1" cellpadding="5" cellspacing="0">
 *     <tr>
 *         <td>原始串</td>
 *         <td>英文串</td>
 *         <td>API串</td>
 *         <td>中文串</td>
 *         <td>需求串</td>
 *     </tr>
 *     <tr>
 *         <td>rawstr</td>
 *         <td>enstr</td>
 *         <td>apistr</td>
 *         <td>cnstr</td>
 *         <td>okstr</td>
 *     </tr>
 *     <tr>
 *         <td>before</td>
 *         <td>api</td>
 *         <td>log</td>
 *         <td>after</td>
 *         <td>special</td>
 *     </tr>
 * </table>
 * <p>
 * 
 * @author he_pe 2017-11-18
 */
public interface Translate {

	static final Logger logger = LoggerFactory.getLogger(Translate.class);
	static final Translate translate = null;
	
	// 必须重写的两个方法: api接口方法, 后处理方法
	String api(String enstr) throws IOException;
	String after(String apistr);
	
	/**
	 * 作为工具类: 对外提供的静态翻译方法
	 * @param enstr 英文字符串
	 * @param translateClass 指定翻译的类(百度,谷歌,有道)
	 * @return 中文字符串
	 */
	static <T extends Translate> String translate(String enstr,Class<T> translateClass){
		if(StringUtils.isBlank(enstr)) return "";
		try {
			// 考虑单例,多线程安全问题分析: 即使重复赋值一次,也没有关系,因此不再加锁
			Translate instance = translate == null? translateClass.newInstance() : translate;
			String apistr = instance.api(enstr);
			logger.debug(apistr);
			String cnstr = instance.after(apistr);
			return cnstr;
		}catch(Exception e) {
			return AppConst.TRANS_ERROR;
		}
	}
}
