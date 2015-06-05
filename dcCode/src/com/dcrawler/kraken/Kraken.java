package com.dcrawler.kraken;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dcrawler.kraken.component.TaskQueue;
import com.dcrawler.kraken.component.entry.DefaultTask;
import com.dcrawler.kraken.component.entry.TemplateTask;
/**
 * 单例进程
 * 
 * @author Bin Xu
 *
 */
public class Kraken {
	private static Log LOG = LogFactory.getLog(Kraken.class);
	
	// 一个TemplateTask映射一个TaskQueue
	// 用Task's ID 作为key:templateTask->id
	private static ConcurrentHashMap<String,TaskQueue> taskQueueMap = new ConcurrentHashMap<String, TaskQueue>();
	
	
	/**
	 * 多线程处理Worker
	 * Worker run support
	 */
	private List<Worker> workers;
	private ThreadPoolExecutor executorService;
	//private String status = "";// 执行状态信息
	
	
	// 单例
	private static final Kraken instance = new Kraken();
	private Kraken(){}
	public static Kraken getInstance(){		
		return instance;
	}
	
	/**
	 * 获得随即数字生成TemplateTask->ID
	 * @return
	 */
	public String getRd(){
		String s = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random rd = new Random();
		int i = rd.nextInt(s.length());
		StringBuilder sb = new StringBuilder();
		sb.append(s.charAt(i));
		i = rd.nextInt(s.length());
		sb.append(s.charAt(i));
		i = rd.nextInt(s.length());
		sb.append(s.charAt(i));
		i = rd.nextInt(s.length());
		sb.append(s.charAt(i));
		i = rd.nextInt(s.length());
		sb.append(s.charAt(i));
		return sb.toString();
	}
	
	public void addTaskToQueue(List<TemplateTask> templateTasks){
		for (TemplateTask templateTask : templateTasks) {
			if(templateTask.getCrawlerId()!=null){
				if(templateTask.getId() == null || (templateTask.getId()).trim().equalsIgnoreCase("")){
					templateTask.setId(templateTask.getCrawlerId() + "_" + System.currentTimeMillis() + "_" + getRd());
				}
				String id = templateTask.getId();
				TaskQueue taskQueue = new TaskQueue();
				taskQueue.setId(id);
				taskQueueMap.put(id, taskQueue);
				
				List<DefaultTask> tasks = templateTask.getGenetatedTasks();
				if(tasks != null){
					taskQueue.addTask(tasks);
				}
			}
		}
	}
	
	public void init(List<TemplateTask> templateTasks){
		
//		initTaskQueueSchdulerMap(list);
		addTaskToQueue(templateTasks);
		
		if(templateTasks!=null && templateTasks.size()>0 && taskQueueMap!=null && (taskQueueMap.values()).size()>0){
			workers = new ArrayList<Worker>((taskQueueMap.values()).size());
			
			int workId = 0;
			Iterator<TaskQueue> iterator = (taskQueueMap.values()).iterator();
			while(iterator.hasNext()){
				Worker worker = new Worker(workId++);
				worker.setTaskQueue(iterator.next());
				workers.add(worker);
			}
			
		}
		
		if(workers!=null && workers.size()>0){
			executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(workers.size());
		}
		
	}
	
	
	public void startWorkers(List<TemplateTask> templateTasks){
		
		// 初始化队列
		
//		initTaskQueueMap(list);
		init(templateTasks);
		if(workers!=null && workers.size()>0 && executorService!=null){
			for(Worker worker : workers){
				executorService.execute(worker);
				LOG.warn((String.format("Queue %s Worker %d Starting...", worker.getTaskQueue() == null? "null":worker.getTaskQueue().getId(), worker.getId())));
			}
		}
	}
	
	public static void main(String[] args) {
		
		List<TemplateTask> templateTasks = new ArrayList<TemplateTask> ();
		
		for (int i = 0; i < 1; i++) {
			TemplateTask dt = new TemplateTask();
			dt.setBrands("jd");
			dt.setCrawlerId("crawler_jd");
			dt.setKeywords("天猫");
			dt.setPriorities("0");
			dt.setTags("t恤男");
			dt.setValidateRobots(false);
			// http://search.jd.com/Search?keyword=t%E6%81%A4%20%E7%94%B7&enc=utf-8&suggest=0
			dt.setUri("http://search.jd.com/Search?keyword=t%E6%81%A4%20%E7%94%B7&enc=utf-8&suggest=0");
			templateTasks.add(dt);
		}
		Kraken.getInstance().startWorkers(templateTasks);
		
		
	}
	
}


































