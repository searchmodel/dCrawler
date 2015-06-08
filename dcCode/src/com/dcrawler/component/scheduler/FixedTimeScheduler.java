package com.dcrawler.component.scheduler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dcrawler.component.TaskQueue;
import com.dcrawler.component.entry.AbstractTask;
import com.dcrawler.component.entry.DefaultTask;

/**
 * 有固定时间的调度
 * 
 * @author Bin Xu
 * 
 */
public class FixedTimeScheduler implements ITaskScheduler {

	private static Log LOG = LogFactory.getLog(FixedTimeScheduler.class);

	private int time;

	private boolean dispose = false;

	private List<DefaultTask> _tasks;

	private TaskQueue taskQueue;

	private Timer timer = new Timer();
	
	public FixedTimeScheduler(int time) {
		this.time = time;
		_tasks = new ArrayList<DefaultTask>(200);
	}

	public void addTask(AbstractTask seedTask) {
		// TODO Auto-generated method stub
		_tasks.add((DefaultTask) seedTask);
	}

	public void startScheduler() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, time);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		timer.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run() {				
				if(taskQueue!=null && _tasks!=null && _tasks.size()>0){
					for(DefaultTask task : _tasks){
						if(dispose) break;
						taskQueue.addTask(task);
						LOG.warn(String.format("FixedScheduler [%s] Add Task [%s] with TaskSize [%d]", taskQueue.getId(),task.getUri(),_tasks.size()));
					}
				}				
			}
			
		}, c.getTime(), 24*60*60*1000);	

	}

	public void setTaskQueue(TaskQueue taskQueue) {
		this.taskQueue = taskQueue;
	}

	public TaskQueue getTaskQueue() {
		return taskQueue;
	}
	
	public void doDispose(){
		dispose = true;
		timer.cancel();
	}

}
