package com.hpj.apicn.app;


import com.hpj.apicn.util.DocUtil;
import com.hpj.apicn.util.JarUtil;

/**
 * 应用入口类: 翻译Java源文件,然后生成新的sources.jar,javadoc.jar
 * 
 * @author he_pe
 */
public class AppJava {


	/** 翻译的主函数入口  */
	public static void main(String[] args) throws Exception {
		
		/**
		 * 处理流程: 
		 * 
		 * 1.输入参数: 英文源码jar包的名称
		 * 2.缓存模块: [工具类]取得get,put方法(ConcurrentHashMap或Redis)
		 * 3.翻译模块: [接口与实现类]取得翻译函数(内部优先使用缓存数据)
		 * 4.解析模块: [工具类]读取jar包,解析java文件,根据"翻译函数"翻译java文件,生成新的中文源码jar包
		 * 5.文档模块: [工具类]根据中文源码jar包生成html文档,再生成chm文档
		 */
		String enJarName = "D:\\TEMP\\commons-lang3-3.7-sources.jar";
		
		String cnJarName = JarUtil.makeCnJar(enJarName, AppJava::translateJavaContext);
		String cnHtmlDir = DocUtil.makeHtml(cnJarName);
		String cnChmName = DocUtil.makeChm(cnHtmlDir);
		
		System.out.println(cnChmName);
	}
	
	/**
	 * 翻译Java文件内容.
	 * 
	 * @param srcstr: Java文件生成的字符串
	 * @return 翻译其内部注释后的字符串
	 */
	public static String translateJavaContext(String srcstr) {
		// TODO Auto-generated method stub
		return srcstr + "//HPJ";
	}
}
