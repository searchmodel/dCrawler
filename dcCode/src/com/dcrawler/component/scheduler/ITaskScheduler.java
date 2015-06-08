package com.dcrawler.component.scheduler;

import com.dcrawler.component.TaskQueue;
import com.dcrawler.component.entry.AbstractTask;

public interface ITaskScheduler {
	
	void addTask(AbstractTask seedTask);

	void startScheduler();

	public void setTaskQueue(TaskQueue taskQueue);

	public TaskQueue getTaskQueue();

	public void doDispose();
}
