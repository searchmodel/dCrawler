package com.dcrawler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dcrawler.component.IProcessor;
import com.dcrawler.component.TaskQueue;
import com.dcrawler.component.entry.DefaultTask;
import com.dcrawler.util.Config;

/**
 * 多线程处理任务
 * 
 * @author Bin Xu
 * 
 */
public class Worker implements Runnable {

	private static Log LOG = LogFactory.getLog(Worker.class);

	private int id;
	private boolean stop = false;
	private TaskQueue taskQueue;
	private int waitInterval = 10 * 1000;
	
	
	public Worker(int id) {
		this.id = id;
	}

	public void run() {
		DefaultTask task = null;
		IProcessor processor = null;
		try {
			while (!isStop()) {
				if(taskQueue!=null){
					try {
						task = taskQueue.poll();
						
						if(task!=null){

							// 反射得到 IProcess，通过task.getCrawlerId()
							String clsName = Config.getInstance().getValue(task.getCrawlerId());
							Class<?> dc = Class.forName(clsName);
							processor = (IProcessor) dc.newInstance();
							
							if(processor!=null){
								/**
								 * TODO　Should add validateRobots
								 */
								boolean validateRobots = task.isValidateRobots();
								if(!validateRobots){
									// 执行自己实现的代码
									processor.process(task);
								}
							}
						}
						
					} catch (InterruptedException e) {
						e.printStackTrace();
						LOG.warn("TaskQueue Poll Exception");
					} catch (Exception e) {
						e.printStackTrace();
						LOG.error("Process Exception: " + processor == null? "null" : processor.getClass().getCanonicalName(), e);
					} finally{
						if(task!=null){
							task.setLastRunTime(System.currentTimeMillis());
							// TODO 唤醒任务处理，防止有没有进行跳出的任务出现
							// notifyTaskFinish(task, taskQueue, fetchResult);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.warn((String
					.format("Queue %s Worker %d Thread[%s]: failed to enter to sleep %dms",
							taskQueue == null ? "null" : taskQueue.getId(), id,
							Thread.currentThread().getName(), waitInterval)));
		}
	}
	
	
	
	
	/**
	 * 正常停止工作
	 */
	public void notifyWorkerStop(){
		// 调用Worker 单例停止进程
	}
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public TaskQueue getTaskQueue() {
		return taskQueue;
	}

	public void setTaskQueue(TaskQueue taskQueue) {
		this.taskQueue = taskQueue;
	}

}
