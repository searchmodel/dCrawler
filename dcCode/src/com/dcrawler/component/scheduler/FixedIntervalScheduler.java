package com.dcrawler.component.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dcrawler.component.TaskQueue;
import com.dcrawler.component.entry.AbstractTask;
import com.dcrawler.component.entry.DefaultTask;
/**
 * 有时间间隔的调度
 * @author Bin Xu
 *
 */
public class FixedIntervalScheduler implements ITaskScheduler {

	private static Log LOG = LogFactory.getLog(FixedIntervalScheduler.class);

	// 时间间隔
	private int	interval;
	
	private boolean dispose = false;

	// 将DefaultTask添加到TaskQueue
	private List<DefaultTask> _tasks;

	private TaskQueue taskQueue;

	// 使用Timer调度
	private Timer timer = new Timer();
	
	public FixedIntervalScheduler(int interval){
		this.interval = interval;
		_tasks = new ArrayList<DefaultTask>(200);
	}
	
	public void addTask(AbstractTask seedTask) {
		_tasks.add((DefaultTask) seedTask);
	}

	public void startScheduler() {
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if(taskQueue!=null && _tasks!=null && _tasks.size()>0){
					for (DefaultTask task : _tasks){
						if(dispose) break;
						taskQueue.addTask(task);
						LOG.warn(String.format("FixedInterval [%s] Add Task [%s] with TaskSize [%d]", taskQueue.getId(),task.getUri(),_tasks.size()));
					}
				}
			}
			
		}, 0, interval * 1000);
	}

	public void setTaskQueue(TaskQueue taskQueue) {
		this.taskQueue = taskQueue;
	}

	public TaskQueue getTaskQueue() {
		return taskQueue;
	}

	/**
	 * 停止任务
	 */
	public void doDispose() {
		dispose = true;
		timer.cancel();
	}

}
