package com.hpj.apicn.util;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存工具类.<br><br>
 * 
 * 说明: 如果redis可用,则优先使用redis作为缓存,否则使用并发HashMap作为缓存.<br>
 * 原因: 使用redis可以自动处理持久化问题,每次项目启动还可以利用上次的缓存数据,缺点为需要安装redis服务(NoSQL数据库).
 * 
 * @author he_pe
 */
public class CacheUtil {
	
	private static boolean redisOk;
	private static ConcurrentHashMap<String, String> map;
	
	public static String put(String key,String value) {
		return redisOk ? putInRedis(key, value) : putInMap(key, value);
	}
	
	public static String get(String key) {
		return redisOk ? getFromRedis(key) : getFromMap(key);
	}
	
	public static String putInRedis(String key,String value) {
		return null;
	}
	
	public static String getFromRedis(String key) {
		return null;
	}
	
	public static String putInMap(String key,String value) {
		return map.put(key, value);
	}
	
	public static String getFromMap(String key) {
		return map.get(key);
	}
}
