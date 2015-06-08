package com.dcrawler.component.scheduler;

import java.util.ArrayList;
import java.util.List;

import com.dcrawler.component.TaskQueue;
import com.dcrawler.component.entry.AbstractTask;
import com.dcrawler.component.entry.DefaultTask;

/**
 * 只执行一次调度
 * @author Bin Xu
 *
 */
public class OneTimeScheduler implements ITaskScheduler {

	private boolean dispose = false;

	private List<DefaultTask> _tasks;

	private TaskQueue taskQueue;

	public OneTimeScheduler() {
		_tasks = new ArrayList<DefaultTask>(200);
	}

	public void addTask(AbstractTask seedTask) {
		_tasks.add((DefaultTask) seedTask);
	}

	public void startScheduler() {
		if (taskQueue != null && _tasks != null && _tasks.size() > 0) {
			for (DefaultTask task : _tasks) {
				if (dispose)
					break;
				taskQueue.addTask(task);
			}
		}
	}

	public TaskQueue getTaskQueue() {
		return taskQueue;
	}

	public void setTaskQueue(TaskQueue taskQueue) {
		this.taskQueue = taskQueue;
	}

	public void doDispose() {
		dispose = true;
	}

}
