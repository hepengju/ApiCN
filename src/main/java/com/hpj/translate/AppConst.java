package com.hpj.translate;

import java.util.Arrays;
import java.util.List;

/**
 * 应用的常量配置
 * 
 * @author he_pe
 */
public class AppConst {

	/** 翻译过程异常的返回字符串 */
	public static final String TRANS_ERROR = "$$$-API-ERROR-$$$";
	
	
	//------------ 解析html进行翻译的常量设置 [开始]------------ //
	/** 需要翻译的文件扩展名列表 */
	public static final List<String> TRANS_FILES = Arrays.asList("htm","html");
	
	/** 不需要翻译的标签列表 */
	public static final List<String> NOTRANS_TAGS = Arrays.asList("pre","code", "blockquote", "a",  "cite", "tt", "table");
	
	/** 不需要正则表达. 如果需要翻译的标签内部,将不需要翻译的标签替换后,只剩下以下内容则不翻译. */
	public static final String NOTRANS_REGEX = "^[\\d<>/]*$";
	//------------ 解析html进行翻译的常量设置 [结束]------------ //
	
	
	//------------ 解析源代码进行翻译的常量设置 [开始]------------ //
	public static final String JAR = "jar";
	public static final String _CN = "-cn";
	
	public static final List<String> JAVADOC_TAGS = Arrays.asList("");
	public static final List<String> NOTRANS_JAVADOC_TAGS = Arrays.asList("@author","");
	
	//------------ 解析源代码进行翻译的常量设置 [结束]------------ //
	
	
	public static final String srcEncoding = "UTF-8";
	public static final String newEncoding = "UTF-8";
	
}

/*
 * javadoc标签分析
 * 
 * @param          参数 √
 * @return       返回值 √
 * @throws       抛异常 √
 * @author         作者 ×
 * @deprecated     过期 ×
 * @exception      异常 ×
 * @see            参见 ×
 * @version        版本 ×
 * @since        从版本 ×
 * -----------------------------
 * @serial         
 * @serialData 
 * @serialField 
 * ----{@.*}标签内部均不翻译----
 * {@docRoot}  
 * {@inheritDoc}
 * {@link}     
 * {@linkplain}
 * {@value}
 *
 */
