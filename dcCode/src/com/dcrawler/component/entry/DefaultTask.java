package com.dcrawler.component.entry;

/**
 * 任务，每抓取一个网站就是一个任务
 * 
 * @author ias
 * 
 */
public class DefaultTask extends AbstractTask {

	protected String keyword;
	protected String tag;
	protected int priority;// 优先级
	protected int maxPage;// 最大页
	protected String brand;// 品牌

	protected int order;// 排序
	protected long lastRunTime = 0L;// 最后运行时间

	protected TemplateTask templateTask;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public long getLastRunTime() {
		return lastRunTime;
	}

	public void setLastRunTime(long lastRunTime) {
		this.lastRunTime = lastRunTime;
	}

	public TemplateTask getTemplateTask() {
		return templateTask;
	}

	public void setTemplateTask(TemplateTask templateTask) {
		this.templateTask = templateTask;
	}

}
