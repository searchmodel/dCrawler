package com.dcrawler.kraken;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dcrawler.kraken.component.TaskQueue;
import com.dcrawler.kraken.component.entry.DefaultTask;
/**
 * 单例进程
 * 
 * @author ias
 *
 */
public class Kraken {
	private static Log LOG = LogFactory.getLog(Kraken.class);
	
	// 一个TemplateTask映射一个TaskQueue
	// 用Task's ID 作为key
	private static ConcurrentHashMap<String,TaskQueue> taskQueueMap = new ConcurrentHashMap<String, TaskQueue>();
	
	
	/**
	 * 多线程处理Worker
	 * Worker run support
	 */
	private List<Worker> workers;
	private ThreadPoolExecutor executorService;
	private String status = "";// 执行状态信息
	
	
	// 单例
	private static final Kraken instance = new Kraken();
	private Kraken(){}
	public static Kraken getInstance(){		
		return instance;
	}
	
	
	/**
	 * 通过DefaltTask 列表初始化线程池和worker集合
	 * 此时，Kraken必须是单例的
	 * @param list
	 */
	public void init(List<DefaultTask> list){
		
		addTaskToQueue(list);
		
		if(list!=null && list.size()>0 && taskQueueMap!=null && taskQueueMap.values().size()>0){
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
			// 创建固定数量的线程池
			executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(workers.size());
		}
	}
	
	public void initTaskQueueMap(List<DefaultTask> list){
		if(list!=null && list.size()>0){
			TaskQueue tq = new TaskQueue();
			tq.addTask(list);
			
		}
		
	}
	
	public void addTaskToQueue(List<DefaultTask> list){
		for(DefaultTask defaultTask : list){
			String id = defaultTask.getCrawlerId();
			TaskQueue taskQueue = taskQueueMap.get(id);
			if(taskQueue!=null){
				taskQueue.addTask(defaultTask);
			}
		}
	}
	
	public void stopWorkerById(int id) {
		try {
			if (workers != null && workers.size() > 0) {
				for (Worker worker : workers) {
					if (worker.getId() == id) {
						worker.setStop(true);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 判断所有的Worker是否都停止了？
	 * @return
	 */
	public boolean isAllWorkerStopped() {
		boolean result = true;
		if (workers != null && workers.size() > 0) {
			for (Worker worker : workers) {
				if (!worker.isStop()) {
					result = false;
					break;
				}
			}
		}
		return result;
	}
	public void start(){
		
		// 初始化队列
		List<DefaultTask> list = new ArrayList<DefaultTask>();
		for (int i = 0; i < 1; i++) {
			DefaultTask dt = new DefaultTask();
			dt.setBrand("jd");
			dt.setCrawlerId("crawler_jd");
			dt.setKeyword("天猫");
			dt.setPriority(0);
			dt.setValidateRobots(false);
			// http://search.jd.com/Search?keyword=t%E6%81%A4%20%E7%94%B7&enc=utf-8&suggest=0
			dt.setUri("http://search.jd.com/Search?keyword=t%E6%81%A4%20%E7%94%B7&enc=utf-8&suggest=0");
			dt.setTag("t恤男");
			
			dt.setOrder(0);
			list.add(dt);
		}
		
		initTaskQueueMap(list);
		init(list);
		if(workers!=null && workers.size()>0 && executorService!=null){
			for(Worker worker : workers){
				executorService.execute(worker);
				LOG.warn((String.format("Queue %s Worker %d Starting...", worker.getTaskQueue() == null? "null":worker.getTaskQueue().getId(), worker.getId())));
			}
		}
	}
	
	public static void main(String[] args) {
		
		
		Kraken.getInstance().start();
		
		
	}
	
}


































