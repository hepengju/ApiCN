package com.hpj.apicn.translate.impl;

import java.io.IOException;
import java.net.URLEncoder;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.hpj.apicn.translate.Translate;

/**
 * 谷歌翻译: 参考项目 chmTranslate 的代码,用的可能是其他开发者的帐号(也可能是免费的,因为看到tk校验).<br><br>
 *  
 * <a href="https://gitee.com/xiagao/chmFanYi">项目 chmTranslate的地址</a>
 * 
 * @author he_pe
 */
@Deprecated
public class GoogleTranslateCHM implements Translate {
	
    private static ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
    private static Invocable invocable;
    
    static {
        String script = "function tk(a) {"
                + "var TKK = ((function() {var a = 561666268;var b = 1526272306;return 406398 + '.' + (a + b); })());\n"
                + "function b(a, b) { for (var d = 0; d < b.length - 2; d += 3) { var c = b.charAt(d + 2), c = 'a' <= c ? c.charCodeAt(0) - 87 : Number(c), c = '+' == b.charAt(d + 1) ? a >>> c : a << c; a = '+' == b.charAt(d) ? a + c & 4294967295 : a ^ c } return a }\n"
                + "for (var e = TKK.split('.'), h = Number(e[0]) || 0, g = [], d = 0, f = 0; f < a.length; f++) {"
                + "var c = a.charCodeAt(f);"
                + "128 > c ? g[d++] = c : (2048 > c ? g[d++] = c >> 6 | 192 : (55296 == (c & 64512) && f + 1 < a.length && 56320 == (a.charCodeAt(f + 1) & 64512) ? (c = 65536 + ((c & 1023) << 10) + (a.charCodeAt(++f) & 1023), g[d++] = c >> 18 | 240, g[d++] = c >> 12 & 63 | 128) : g[d++] = c >> 12 | 224, g[d++] = c >> 6 & 63 | 128), g[d++] = c & 63 | 128)"
                + "}"
                + "a = h;"
                + "for (d = 0; d < g.length; d++) a += g[d], a = b(a, '+-a^+6');"
                + "a = b(a, '+-3^+b+-f');"
                + "a ^= Number(e[1]) || 0;"
                + "0 > a && (a = (a & 2147483647) + 2147483648);"
                + "a %= 1E6;"
                + "return a.toString() + '.' + (a ^ h)\n"
                + "}";
        try {
            engine.eval(script);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        invocable = (Invocable) engine;
    }
    
    // 谷歌翻译特有的校验
    private static String calculateTk(String str) {
        String tk = null;
        try {
            tk = (String) invocable.invokeFunction("tk", str);
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return tk;
    }
    
	@Override
	public String api(String query) throws IOException{
		// 客户端
		CloseableHttpClient client = HttpClients.createMinimal();
		
		// 准备url的参数
		String tk = calculateTk(query); 
		String url = String.format("https://translate.googleapis.com/translate_a/t" +
                "?anno=3&client=te_lib&format=html&v=1.0" +
                "&key=AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw" +
                "&logld=vTE_20170327_01" +
                "&sl=en" +
                "&tl=zh-CN" +
                "&sp=nmt" +
                "&tc=4" +
                "&sr=1" +
                "&tk=%s&mode=1", tk);
		
		HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("User-Agent", "Apache-HttpClient/4.5.2 (Java/1.8.0_91)");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        
        // 设置post参数(查询的字符串)
        String postParam = "q=" + URLEncoder.encode(query, "UTF-8");
		httpPost.setEntity(new StringEntity(postParam));
		
		// 发送Post请求,得到返回结果
		try(CloseableHttpResponse response = client.execute(httpPost)){
			return EntityUtils.toString(response.getEntity());
		}
	}
	
	@Override
	public String after(String apistr)  {
		// 参考chmTranslate项目,可能是特殊情况,暂时也加入
		apistr.replace("\\u003c", "<").replace("\\u003e", ">");
		
		// 返回之前去除引号
		apistr = apistr.substring(1, apistr.length() - 1);
		return apistr;
	}
	
	
}
