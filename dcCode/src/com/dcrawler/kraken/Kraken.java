package com.dcrawler.kraken;

import java.util.concurrent.ConcurrentHashMap;

import com.dcrawler.kraken.component.TaskQueue;

public class Kraken {
	
	// 一个TemplateTask映射一个TaskQueue
	// 用Task's ID 作为key
	private static ConcurrentHashMap<String,TaskQueue> taskQueueMap;
	
	
	
	
	
}
