package com.dcrawler.util;

import java.io.IOException;
import java.util.Properties;

public class Config {
	private static Config instance;
	private static Properties p;
	
	private Config(){
	}
	
	
	public static Config getInstance(){
		if(p == null){
			p = new Properties();
			
		}
		if(instance == null){
			instance = new Config();
		}
		try {
			p.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("Crawler.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return instance;
	}
	
	public String getValue(String key){
		System.out.println(p.size());
		return (String) p.get(key);
	}
	public static void main(String[] args) {
		System.out.println(Config.getInstance().getValue("crawler_jd"));
	}
}
