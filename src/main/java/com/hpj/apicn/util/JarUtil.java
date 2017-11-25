package com.hpj.apicn.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hpj.apicn.app.AppConst;

/**
 * Jar包处理工具
 * 
 * @author he_pe
 */
public class JarUtil {

	private static final Logger logger = LoggerFactory.getLogger(JarUtil.class);
	
	/**
	 * 对外提供的产生中文jar包的方法
	 * 
	 * @param enJarName 英文jar包的名称
	 * @param translateFunction 翻译函数
	 * @return 中文jar包的文件名称
	 * @throws Exception
	 */
	public static String makeCnJar(String enJarName,UnaryOperator<String> translateFunction) throws Exception {
		return writeJar(translateJar(enJarName, translateFunction), getOutName(enJarName));
	}

	/** 验证jar文件名称 */
	private static void validName(String srcName) throws Exception {
		if(StringUtils.isBlank(srcName)) throw new Exception("Filename is blank: " + srcName);
		if (!new File(srcName).exists()) throw new Exception("File doesn't exist: " + srcName);
		if (!AppConst.JAR.equalsIgnoreCase(FilenameUtils.getExtension(srcName)))
			throw new Exception("File isn't jar: " + srcName);
	}

	/** 取得输出的jar文件名称 */
	private static String getOutName(String srcName) throws Exception {
		validName(srcName);
		String extension = FilenameUtils.getExtension(srcName);
		String baseName = FilenameUtils.getBaseName(srcName);
		String fullPath = FilenameUtils.getFullPath(srcName);
		String newName = fullPath + baseName + AppConst._CN + extension;
		return newName;
	}
	
	/**
	 * 取得翻译后的jarEntry的Map.<br><br>
	 * 
	 * 说明: 考虑jar工具中不应该有翻译的功能,为了解耦考虑将翻译函数作为参数进行传入.
	 * 
	 * @param srcName 源jar包的文件名称
	 * @param translateFunction 翻译函数
	 * @return 翻译后的Map
	 * @throws IOException
	 */
	private static Map<JarEntry, byte[]> translateJar(String srcName,UnaryOperator<String> translateFunction) throws Exception {
		validName(srcName);
		//注意: 考虑某些文件是图片等,不适合转换为字符串,因此Value值采用byte[],而不是String
		Map<JarEntry,byte[]> newEntryMap = new HashMap<>();
		
		//取得jarFile,使用try-with-resources语句自动关流
		try (JarFile srcJar = new JarFile(srcName);) {
			logger.info("Translate begin: {}", srcJar.getName());
			
			//取得jar包中的所有目录和文件
			Enumeration<JarEntry> entries = srcJar.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				String entryName = entry.getName();
				
				//目录处理: 保持原样
				if (entry.isDirectory()) {
					newEntryMap.put(entry, new byte[0]);
					logger.debug("Copied     dir : {}", entryName);
					continue;
				} 
				
				//文件处理: 取得流
				try (InputStream is = srcJar.getInputStream(entry);) {
					boolean isJavaFile = entryName.endsWith(".java");
					//普通文件处理: 复制流
					if (!isJavaFile) {
						newEntryMap.put(entry, IOUtils.toByteArray(is));
						logger.debug("Copied     file: {}", entryName);
						continue;
					}
					//Java文件处理 : 翻译字符串,生成新流
					String srcstr = IOUtils.toString(is, AppConst.srcEncoding);
					String newstr = translateFunction.apply(srcstr); //翻译!!
					newEntryMap.put(new JarEntry(entryName), newstr.getBytes(AppConst.newEncoding));
					logger.debug("Translated file: {}", entryName);
				}
			}
		}
		
		return newEntryMap;
	}


	/** 
	 * 统一写入新的jar文件.
	 * 
	 * <ul>
	 * 	<li>时机: 所有的Java文件都翻译完成之后</li>
	 * 	<li>原因: 避免长时间占用输出流资源</li>
	 * </ul>
	 * 
	 */
	private static String writeJar(Map<JarEntry, byte[]> newEntryMap,String outName) throws IOException, FileNotFoundException {
		try (JarOutputStream out = new JarOutputStream(new FileOutputStream(outName));) {
			newEntryMap.forEach((entry, bytes) -> {
				try {
					out.putNextEntry(entry);
					out.write(bytes);
				} catch (IOException e) {e.printStackTrace();}
			});
		}
		logger.info("New jar: {}", outName);
		return outName;
	}

}
