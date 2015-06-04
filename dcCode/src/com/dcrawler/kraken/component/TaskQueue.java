package com.dcrawler.kraken.component;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TaskQueue {
	
	private static Log LOG = LogFactory.getLog(TaskQueue.class);
	
	// 作为任务队列的key（Kraken中维护）
	private String id;
	
	// 任务Task队列，排序
	Comparator<DefaultTask> cmp = new Comparator<DefaultTask>(){
		//a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second. 
		public int compare(DefaultTask o1, DefaultTask o2) {
			if(o2.getPriority() == o1.getPriority()){
				if (o1.getLastRunTime() == o2.getLastRunTime()) {
					return o1.getOrder() - o2.getOrder();
				} else {
					if (o2.getLastRunTime() >= o1.getLastRunTime()) {
						return 1;
					} else {
						return -1;
					}
				}
			}else{
				return o2.getPriority() - o1.getPriority();
			}
		}
	};
	// 任务队列信息
	private PriorityBlockingQueue<DefaultTask> priorityQueue = new PriorityBlockingQueue<DefaultTask>(1000,cmp);
	// 维护一个url列表，防止重复使用的，syn
	private Set<String> uris = Collections.synchronizedSet(new HashSet<String>(1000));
	
	/**
	 * 添加任务到 priorityQueue
	 * @param tasks
	 */
	public void addTask(DefaultTask... tasks) {
		if(tasks != null && tasks.length > 0){
			// 循环所有的任务
			for(DefaultTask task : tasks){
				if(task!=null && task.getUri()!=null){
					LOG.warn(String.format("TaskQueue %s Add Task: %s", id, task.getUri()));
					if(uris.contains(task.getUri())){
						LOG.warn(String.format("Task [%s] already in TaskQueue [%s]", task.getUri(), id));
						continue;
					}
					priorityQueue.add(task);
					uris.add(task.getUri());
				}			
			}
		}
	}
	/**
	 * 添加任务到 priorityQueue 通过集合方式添加
	 * @param tasks
	 */
	public void addTask(List<DefaultTask> tasks) {
		if(tasks!=null && tasks.size()>0){
			for(DefaultTask task : tasks){
				if(task!=null && task.getUri()!=null){
					LOG.warn(String.format("TaskQueue %s Add Task: %s", id, task.getUri()));
					if(uris.contains(task.getUri())){
						LOG.warn(String.format("Task [%s] already in TaskQueue [%s]", task.getUri(), id));
						continue;
					}
					priorityQueue.add(task);
					uris.add(task.getUri());
				}
			}
		}
	}
	
	// 取出一个任务执行 uris减少 ， 任务减少
	public DefaultTask poll() throws InterruptedException{
		
		if(priorityQueue==null || priorityQueue.isEmpty()){
			return null;
		}else{
			DefaultTask task = priorityQueue.poll();
			if(uris.contains(task.getUri())){
				uris.remove(task.getUri());
			}
			return task;
		}
	}
	
	/**
	 *  终止所有任务
	 */
	public void stop(){
		if(priorityQueue!=null && uris!=null){
			priorityQueue.clear();
			uris.clear();
		}
	}
	/**
	 * 任务数量统计
	 * @return
	 */
	public int size(){
		if(priorityQueue == null || uris == null){
			return 0;
		}
		return uris.size();
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
}
