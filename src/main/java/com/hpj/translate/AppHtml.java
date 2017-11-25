package com.hpj.translate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hpj.translate.impl.BaiduTranslate;

/**
 * 应用入口类: 翻译html
 * 
 * @author he_pe
 */
@Deprecated
public class AppHtml {
	
	// 指定源文件夹路径和新文件夹路径
	private static String srcPath = "D:\\API\\test\\commons-lang3-3.7\\apidocs";
	private static String newPath = "D:\\API\\test\\commons-lang3-3.7\\apidocs-cn";
	
	/** 翻译的主函数入口 */
	public static void main(String[] args) throws IOException {
		
		// 1.创建新的目录
		FileUtils.forceMkdir(new File(newPath));
		
		// 2.取得所有文件(分为两部分: 需要翻译的,不需要翻译的)
		Map<Boolean, List<File>> filesMap = Files.walk(Paths.get(srcPath)).map(Path::toFile).filter(File::isFile)
			.collect(Collectors.partitioningBy(f -> AppConst.TRANS_FILES.contains(FilenameUtils.getExtension(f.getName()).toLowerCase())));
		
		// 3.将不需要翻译的文件复制到新的目录
		filesMap.get(false).forEach(srcFile -> {
			File newFile = getNewFile(srcFile);
			try {FileUtils.copyFile(srcFile, newFile);} catch (IOException e) {}
		});
		
		// 4.将需要翻译的文件进行翻译后,写入到新的文件(并行执行)
		filesMap.get(true).parallelStream().forEach(srcFile -> {
			File newFile = getNewFile(srcFile);
			try {
				String srcContent = FileUtils.readFileToString(srcFile, AppConst.srcEncoding);
				String newContent = htmlTrans(srcContent);
				FileUtils.writeStringToFile(newFile, newContent, AppConst.newEncoding);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		});
	}

	private static String htmlTrans(String srcContent) {
		Document document = Jsoup.parse(srcContent);
		
		//CSS选择器: class=block的节点,class=blockList下的dd节点
		Elements blockList = document.select(".block,.blockList dd");
		
		blockList.forEach(e -> {
			//节点不需要翻译,不作处理
			if(isNoTransTag(e)) return;
			
			//节点需要翻译,但其他所有节点都不需要翻译,不作处理
			if(e.getAllElements().stream().allMatch(ee -> isNoTransTag(ee))) return;
			
			//将节点的子节点中不需要翻译的内容,用一串唯一的数字(当前纳秒)占据,等待翻译完成后,再替换回来.
			Map<String,String> tempMap = new HashMap<>();
			e.getAllElements().forEach(ee -> {
				if(isNoTransTag(ee)) {
					String nano = String.valueOf(System.nanoTime());
					tempMap.put(nano, ee.html());
					ee.html(nano);
				}
			});
			
			//传入英文,返回中文并设置
			e.html(Translate.translate(e.html(), BaiduTranslate.class));
			e.html(e.html());
			//翻译完成,替换回来子节点中不需要翻译的内容
			e.getAllElements().forEach(ee -> {
				if(isNoTransTag(ee)) {
					ee.html(tempMap.get(ee.html()));
				}
			});
			
		});
		
		//4.1 源文件html进行解析,找到需要翻译的标签内容,进行翻译并返回新的文件内容
		String newContent = document.html();
		return newContent;
	}

	/** 判断是否是不翻译的标签 */
	private static boolean isNoTransTag(Element ee) {
		return AppConst.NOTRANS_TAGS.contains(ee.tagName().toLowerCase());
	}

	/** 根据源文件找到新文件(替换源路径为新路径) */
	private static File getNewFile(File srcFile) {
		File destFile = new File(srcFile.getAbsolutePath().replace(srcPath, newPath));
		return destFile;
	}
}
