package com.hpj.translate;

/**
 * 翻译项目所需的服务商密钥.<br><br>
 * 
 * <ul>
 * 	<li><a herf="http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer">百度翻译申请</a></li>
 * 	<li><a herf="http://ai.youdao.com/docs/doc-trans-api.s#p02">有道翻译申请</a></li>
 * 	<li><a herf="https://cloud.google.com/translate/docs/auth">谷歌翻译申请</a></li>
 * </ul>
 * 
 * @author he_pe
 */
public class AppKey {
	
	/** 百度应用  */
	public static final String BAIDU_APP = "20171118000096634";
	/** 百度密钥  */
	public static final String BAIDU_KEY = "PvGa7B_W_ZbPrHPzh_xk";
	/** 百度主机  */
	public static final String BAIDU_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";
	
	
	/** 有道应用  */
	public static final String YOUDAO_APP = "20080fa5cdeabf4f";
	/** 有道密钥  */
	public static final String YOUDAO_KEY = "7PtHNHlGadOUc4fkDSz8DVTn24PjppNA";
	/** 有道主机  */
	public static final String YOUDAO_HOST = "http://openapi.youdao.com/api";
	
	
	/** 谷歌密钥  */
	public static final String GOOGLE_KEY = "AIzaSyBw9ZpH6OJnvPFltwB-qHMB5ZU1qTIwGjw";
	/** 谷歌主机  */
	public static final String GOOGLE_HOST = "https://translation.googleapis.com/language/translate/v2";	
	
}
