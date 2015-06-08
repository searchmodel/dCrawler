package com.dcrawler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dcrawler.component.TaskQueue;
import com.dcrawler.component.entry.DefaultTask;
import com.dcrawler.component.entry.TemplateTask;
import com.dcrawler.component.scheduler.FixedIntervalScheduler;
import com.dcrawler.component.scheduler.FixedTimeScheduler;
import com.dcrawler.component.scheduler.ITaskScheduler;
import com.dcrawler.component.scheduler.OneTimeScheduler;
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
	private String status = "";// 执行状态信息
	
	
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
				
				String id = templateTask.getId();
				TaskQueue taskQueue = taskQueueMap.get(id);
				ITaskScheduler scheduler = taskSchedulerMap.get(id);
				
				List<DefaultTask> tasks = templateTask.getGenetatedTasks();
				
				// TODO
				if(taskQueue!=null){
					if(scheduler!=null && scheduler instanceof OneTimeScheduler){
						taskQueue.addTask(tasks);
					}
				}
				
				if(scheduler!=null){
					Iterator<DefaultTask> it = tasks.iterator();
					while(it.hasNext()){
						scheduler.addTask(it.next());
					}
				}
				
			}
		}
	}
	
	/**
	 * 调度核心代码
	 * @param list
	 * 
	 */
	public void initTaskQueueSchdulerMap(List<TemplateTask> templateTasks){
		if(templateTasks == null || templateTasks.size() == 0){
			LOG.error("Null Template Task to be run");
			return;
		}
		if(taskQueueMap == null){
			taskQueueMap = new ConcurrentHashMap<String,TaskQueue>(1000);
		}
		taskQueueMap.clear();
		
		if(taskSchedulerMap == null){
			taskSchedulerMap = new ConcurrentHashMap<String,ITaskScheduler>(1000);
		}
		taskSchedulerMap.clear();
		
		for(TemplateTask templateTask : templateTasks){
			if(templateTask.getCrawlerId()!=null){
				if(templateTask.getId() == null || (templateTask.getId()).trim().equalsIgnoreCase("")){
					templateTask.setId(templateTask.getCrawlerId() + "_" + System.currentTimeMillis() + "_" + getRd());
				}
				
				String id = templateTask.getId();
				TaskQueue taskQueue = new TaskQueue();
				taskQueue.setId(id);
				taskQueueMap.put(id, taskQueue);
				
				ITaskScheduler scheduler = null;
				if(templateTask.getSchedulerName()!=null){
					String schedulerName = templateTask.getSchedulerName();					
					if(schedulerName.equalsIgnoreCase("OneTime")){
						scheduler = new OneTimeScheduler();
					}else if(schedulerName.equalsIgnoreCase("FixedInterval")){
						// 默认24小时执行一次
						int intervalS = 24*60*60;
						if(templateTask.getSchedulerVaule()!=null){
							intervalS = Integer.parseInt(templateTask.getSchedulerVaule());
						}
						scheduler = new FixedIntervalScheduler(intervalS);
					}else if(schedulerName.equalsIgnoreCase("FixedTime")){
						// 默认1点执行
						int	time = 1;
						if(templateTask.getSchedulerVaule()!=null){
							time = Integer.parseInt(templateTask.getSchedulerVaule());
						}
						if(time <0 || time>24){
							time = 1;
						}
						scheduler = new FixedTimeScheduler(time);
					}else{
						LOG.info("scheduler is null.");
					}
				}
				if(scheduler!=null){
					scheduler.setTaskQueue(taskQueue);
					taskSchedulerMap.put(id, scheduler);
				}
			}
		}
	}
	
	public void init(List<TemplateTask> templateTasks){
		
		initTaskQueueSchdulerMap(templateTasks);
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
		// 初始化线程池
		if(workers!=null && workers.size()>0){
			executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(workers.size());
		}
		
	}
	
	private static ConcurrentHashMap<String,ITaskScheduler> taskSchedulerMap = null;
	
	
	/**
	 * 启动Worker,线程
	 * @param templateTasks
	 */
	public void startWorkers(List<TemplateTask> templateTasks){
		
		// 初始化队列
		
		init(templateTasks);
		if(workers!=null && workers.size()>0 && executorService!=null){
			
//			TODO 
			if(taskSchedulerMap!=null){			
				Iterator<ITaskScheduler> it = taskSchedulerMap.values().iterator();
				while(it.hasNext()){
					ITaskScheduler scheduler = it.next();
					scheduler.startScheduler();
				}
			}
			
			for(Worker worker : workers){
				executorService.execute(worker);
				LOG.warn((String.format("Queue %s Worker %d Starting...", worker.getTaskQueue() == null? "null":worker.getTaskQueue().getId(), worker.getId())));
			}
			status = "Running";
		}else{
			status = "Error parsing task input";
		}
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
			dt.setSchedulerName("FixedInterval");
			dt.setSchedulerVaule("1");
			// http://search.jd.com/Search?keyword=t%E6%81%A4%20%E7%94%B7&enc=utf-8&suggest=0
			dt.setUri("http://search.jd.com/Search?keyword=t%E6%81%A4%20%E7%94%B7&enc=utf-8&suggest=0");
			templateTasks.add(dt);
		}
		Kraken.getInstance().startWorkers(templateTasks);
		
		
	}
	
}


































