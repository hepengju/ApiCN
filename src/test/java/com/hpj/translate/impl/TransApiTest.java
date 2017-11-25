package com.hpj.translate.impl;

import org.junit.BeforeClass;
import org.junit.Test;

import com.hpj.translate.Translate;

/**
 * 测试翻译接口API
 * 
 * @author he_pe
 */
public class TransApiTest {
	// 英文
	// Apache Commons Lang 3.7 API --> index.html --> Description of org.apache.commons.lang3 Package
	private static String enstr = "Provides highly reusable static utility methods, "
			                    + "chiefly concerned with adding value to the java.lang classes.";
	
	// 先输出英文
	@BeforeClass
	public static void printEnstr() {
		System.out.println("英文原文: " + enstr);
	}
	
	@Test public void testBaidu() {
		System.out.printf("百度翻译: %s\n", Translate.translate(enstr,BaiduTranslate.class));
	}
	
	@Test public void testYoudao() {
		System.out.printf("有道翻译: %s\n", Translate.translate(enstr,YoudaoTranslate.class));
	}
	
	@Test public void testGoogle() {
		System.out.printf("谷歌翻译: %s\n", Translate.translate(enstr,GoogleTranslate.class));
	}
	
}
