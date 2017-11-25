package com.hpj.translate.impl;

/**
 * 代码编写过程的临时测试区
 * 
 * @author he_pe
 */
public class TempCodeTest {

//	@Test
//	public void fun01() {
//		String[] strs = {"1","2"};
//		String join = StringUtils.join(strs, ",");
//		System.out.println(join);
////		String join2 = String.format("%s,%s", strs);
////		System.out.println(join2);
//		String join3 = Arrays.stream(strs).collect(Collectors.joining(","));
//		System.out.println(join3);
//	}
//	//@Test
//	public void jar() throws Exception {
//		// 源名称 --> 新名称: *-cn.jar
//		String srcName = "D:\\TEMP\\commons-lang3-3.7-sources.jar";
//		String outName = getOutJarName(srcName);
//
//		try (JarFile srcJar = new JarFile(srcName);
//				JarOutputStream out = new JarOutputStream(new FileOutputStream(outName));) {
//
//			Enumeration<JarEntry> entries = srcJar.entries();
//			while (entries.hasMoreElements()) {
//				JarEntry entry = entries.nextElement();
//				if (!entry.isDirectory() && entry.getName().endsWith(".java")) {
//					String srcstr = "";
//					try (InputStream is = srcJar.getInputStream(entry);) {
//						srcstr = IOUtils.toString(is, "UTF-8");
//					}
//					String newstr = srcstr + "// HPJ " + LocalDateTime.now();
//					JarEntry newentry = new JarEntry(entry.getName());
//					out.putNextEntry(newentry);
//					out.write(newstr.getBytes("UTF-8"));
//				}else {
//					out.putNextEntry(entry);
//				}
//				out.closeEntry();
//			}
//
//		}
//	}
//
//	private String getOutJarName(String srcName) throws Exception {
//		String extension = FilenameUtils.getExtension(srcName);
//		String baseName = FilenameUtils.getBaseName(srcName);
//		String fullPath = FilenameUtils.getFullPath(srcName);
//		if("jar".equalsIgnoreCase(FilenameUtils.getExtension(extension))) throw new Exception("文件不是jar文件");
//		String newName = fullPath + baseName + "-cn." + extension;
//		return newName;
//	}
//
//	private void translateComment(Comment c) {
//		String srcContent = c.getContent();
//		@SuppressWarnings("unused")
//		String traContent = srcContent.replaceAll("", "");
//		String newContent = "///////////翻译的Javadoc注释/////////";
//		if(c instanceof JavadocComment) {
//			c.setContent(newContent + System.lineSeparator() + srcContent);
//		}else {
//			c.setContent(newContent + System.lineSeparator() + srcContent);
//		}
//		
//	}
//
//	private void translatePackageDeclaration(PackageDeclaration p) {
//		
//	}
//	@Test
//	public void javaParser() throws IOException {
//		String srcName = "d:/TEMP/SerializationException.java";
//		String srcContent = FileUtils.readFileToString(new File(srcName), Charset.forName("utf-8"));
//		
//		//1.取得代表Java文件的编译单元
//		CompilationUnit cunit = JavaParser.parse(srcContent);
//		
//		//2.翻译包声明(版本信息)和所有注释
//		cunit.getPackageDeclaration().ifPresent(this::translatePackageDeclaration);
//		cunit.getComments().forEach(this::translateComment);
//		System.out.println(cunit.toString());
//		
//	}
//	//@Test
//	public void fileHandle() throws IOException {
//		String dir = "D:\\API\\test\\commons-lang3-3.7\\apidocs-cnc";
//		//Path path = Paths.get(dir);
//		//System.out.println(path.getFileName());
//		//System.out.println(path.getName(-1));
//		System.out.println(FilenameUtils.getExtension(dir));
//		FileUtils.forceDelete(new File(dir));
//		FileUtils.forceMkdir(new File(dir));
//	}
//	
//	//@Test
//	public void regex() {
//		String regex = "^[\\d<>/]*$";
//		String str = "<28609220565512></28609220565512>";
//		System.out.println(str.matches(regex));
//	}
//	
//	//@Test
//	public void jsoupHtml() throws IOException {
//		String srcName = "D:\\Number.html";
//		String newName = "D:\\NumberCN.html";
//		String srcContent = FileUtils.readFileToString(new File(srcName), AppConst.srcEncoding);
//		
//		Document document = Jsoup.parse(srcContent);
//		
//		//CSS选择器: class=block的节点,class=blockList下的dd节点
//		Elements blockList = document.select(".block,.blockList dd");
//		
//		blockList.forEach(e -> {
//			//节点不需要翻译,不作处理
//			if(isNoTransTag(e)) return;
//			
//			//节点需要翻译,但其他所有节点都不需要翻译,不作处理
//			if(e.getAllElements().stream().allMatch(ee -> isNoTransTag(ee))) return;
//			
//			//将节点的子节点中不需要翻译的内容,用一串唯一的数字(当前纳秒)占据,等待翻译完成后,再替换回来.
//			Map<String,String> tempMap = new HashMap<>();
//			e.getAllElements().forEach(ee -> {
//				if(isNoTransTag(ee)) {
//					String nano = String.valueOf(System.nanoTime());
//					tempMap.put(nano, ee.html());
//					ee.html(nano);
//				}
//			});
//			
//			
//			//传入英文,返回中文并设置
//			e.html(Translate.translate(e.html(), BaiduTranslate.class));
//			e.html(e.html());
//			//翻译完成,替换回来子节点中不需要翻译的内容
//			e.getAllElements().forEach(ee -> {
//				if(isNoTransTag(ee)) {
//					ee.html(tempMap.get(ee.html()));
//				}
//			});
//			
//		});
//		
//		String html = document.html();
//		
//		FileUtils.writeStringToFile(new File(newName), html, AppConst.newEncoding);
//		
//	}
//	
//	public boolean isNoTransTag(Element e) {
//		return AppConst.NOTRANS_TAGS.contains(e.tagName().toLowerCase());
//	}
}
