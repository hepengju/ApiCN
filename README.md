# ApiCN
使用机器翻译与人工审核完成JavaApi文档的中文化

## 项目背景
- **Apache Commons Lang的学习**
    * 简介: java.lang包的增强,强大简约优雅
    * 学习: 为了提高代码水平与规范,准备进行源代码级别的深入学习,学习过程中发现很多方法都代码非常简单,记录笔记很麻烦,用到时查看api文档即可
    * 升级: api文档很小,如果直接翻译成中文api,查看起来会方便很多,发布后也可以给其他人带来方便
    

- **翻译思路**
    * 人工翻译: 参考网上已有的说明直接人工翻译
    * 机器翻译: 调用第三方翻译接口进行翻译
    * 组合翻译: 先使用机器翻译,然后再进行人工审核. 
        - 减轻翻译工作量
        - 保证前后一致性
        - 审核过程即为源码学习过程


 ## 机器翻译思考
- **html的API文档翻译**
    * 简述: 根据已有的api文档找到相应的标签翻译
    * 流程:
        - 输入: 英文html文档
        - 翻译: 中文html文档
        - 生成: chm的api文档
    * 优点: chmTranslate等类似项目已有实现 
        - [chmTranslate](https://gitee.com/xiagao/chmFanYi): 翻译html的api文件,其实现了java7,8,9等api的翻译
    * 缺点: 标签嵌套复杂,简单方案造成的翻译不完善,修改为规范文档工作量巨大


- **java源文件翻译[推荐]**
    * 简述: java源文件中找到需要翻译的注释,进行翻译,然后利用javadoc生成新的api
    * 流程: 
        - 输入: commons-lang3-3.7-sources.jar
        - 翻译: commons-lang3-3.7-sources-cn.jar
        - 生成: 中文html文档
        - 生成: 中文chm文档
    * 优点:
        - 使用JavaParser很容易找到注释对象,翻译后修改对象重新生成Java文件即可
        - 人工审核新的Java文件即为源代码的学习过程
        - 根据新的Java文件,利用javadoc生成新的文档,不会出现某些英文标签遗漏翻译的情况
        - 原有的英文注释便于保留以供参考
        - 甚至可以关联源码直接关联生成的**-cn.jar包,使用更加便捷
    * 缺点: 目前没有找到类似实现,需要自己思考实现各功能


## 项目模块思考与功能实现简要说明
- **翻译模块**
    * 接口: Translate
        - String api(String enstr): 输入英文字符串,发送给翻译服务商,返回服务商响应字符串
        - String after(String apistr): 输入服务商响应字符串(一般为json对象),解析后取出对应的中文字符串
    * 实现类: 仅仅实现接口的两个方法即可
        - 谷歌翻译: GoogleTranslate 
        - 百度翻译: BaiduTranslate  
        - 有道翻译: YoudaoTranalte  
    * 工具类: 由于Java8支持接口中加入默认方法和静态方法,因此不必再新建Translates类.(类比Collection接口,Collections工具类)
        - before: 前处理,主要处理空字符串直接返回,缓存中是否已经有此英文串的翻译等
        - log: 每个实现类记录new出Log对象,并记录,代码重复不够简约,参考AOP思想,在api和after之间加入log方法记录翻译服务商的发送和接收信息
        - **translate**: 供其他模块调用的方法,将before,api,log,after等结合在一起的方法


- **解析模块**
    * jar包读取与写入: Java自带的JarFile类和JarOutputStream类
    * java注释的解析: [JavaParser](https://github.com/javaparser/javaparser)
    * 常量类: AppConst,存储源码编码,Javadoc中不需要翻译的标签(比如@author等信息)
    * 密钥类: AppKey,存储翻译服务商提供的密钥与主机地址


- **缓存模块**
    * redis: 简单使用set和get方法,将英文对应中文存入缓存(内存),并可自动持久化到文件便于下次使用.
    * 优点: 降低重复翻译,提高效率,保证前后一致性等
    

- **文档模块**
    * 源码生成html: javadoc
    * html生成chm: 调用Windows上的HugeChm,EasyChm等软件实现或采用Java相关项目(比如: [javadoc.chm](https://gitee.com/robot/javadoc.chm))生成

