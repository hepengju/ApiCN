package com.hpj.translate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 应用入口类: 翻译Java源文件,然后生成新的sources.jar,javadoc.jar
 * 
 * @author he_pe
 */
public class AppJava {

	private static final Logger logger = LoggerFactory.getLogger(AppJava.class);

	/** 翻译的主函数入口  */
	public static void main(String[] args) throws Exception {
		
		//源名称
		String srcName = "D:\\TEMP\\commons-lang3-3.7-sources.jar";
		
		//写入jar包函数(参数1: 翻译后的jar,参数2: 输出文件名称) --> 其中输出名称默认为: *-cn.jar
		writeJar(translateJar(srcName),getOutName(srcName));
	}
	
	/**
	 * 取得翻译后的jarEntry的Map.
	 * 
	 * @param srcName 源jar包的文件名称
	 * @return 翻译后的Map
	 * @throws IOException
	 */
	private static Map<JarEntry, byte[]> translateJar(String srcName) throws Exception {
		validSrcName(srcName);
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
					String newstr = translateJavaContext(srcstr); //翻译!!
					newEntryMap.put(new JarEntry(entryName), newstr.getBytes(AppConst.newEncoding));
					logger.debug("Translated file: {}", entryName);
				}
			}
		}
		
		return newEntryMap;
	}

	/**
	 * 翻译Java文件内容.
	 * 
	 * @param srcstr: Java文件生成的字符串
	 * @return 翻译其内部注释后的字符串
	 */
	private static String translateJavaContext(String srcstr) {
		// TODO Auto-generated method stub
		return srcstr + "//HPJ";
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
	private static void writeJar(Map<JarEntry, byte[]> newEntryMap,String outName) throws IOException, FileNotFoundException {
		try (JarOutputStream out = new JarOutputStream(new FileOutputStream(outName));) {
			newEntryMap.forEach((entry, bytes) -> {
				try {
					out.putNextEntry(entry);
					out.write(bytes);
				} catch (IOException e) {e.printStackTrace();}
			});
		}
		logger.info("New jar: {}", outName);
	}

	/** 取得输出的jar文件名称 */
	private static String getOutName(String srcName) throws Exception {
		validSrcName(srcName);
		String extension = FilenameUtils.getExtension(srcName);
		String baseName = FilenameUtils.getBaseName(srcName);
		String fullPath = FilenameUtils.getFullPath(srcName);
		String newName = fullPath + baseName + AppConst._CN + extension;
		return newName;
	}

	/** 验证jar文件名称 */
	private static void validSrcName(String srcName) throws Exception {
		if(StringUtils.isBlank(srcName)) throw new Exception("Filename is blank: " + srcName);
		if (!new File(srcName).exists()) throw new Exception("File doesn't exist: " + srcName);
		if (!AppConst.JAR.equalsIgnoreCase(FilenameUtils.getExtension(srcName)))
			throw new Exception("File isn't jar: " + srcName);
	}
}
