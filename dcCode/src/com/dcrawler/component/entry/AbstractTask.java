package com.dcrawler.component.entry;

/**
 * 抽象Task实体类
 * 
 * @author Bin Xu
 */
public class AbstractTask {
	protected String crawlerId;
	protected String uri;
	protected String allDataPriority;
	protected boolean escape = true;
	protected String encoding = "UTF-8";
	protected String runMode;
	protected String dateRange;
	protected String siteName;
	protected String contentType;
	protected String range;

	protected String schedulerName;
	protected String schedulerVaule;

	protected boolean validateRobots = false;

	protected int maxLinkDepth = 4;
	protected String acceptPattern;
	protected String denyPattern;

	protected String isOnlyTotalNum;

	// Generated in Program
	protected int linkDepth = 1;
	protected boolean isStop = false;

	protected int getPage = 0;

	protected String id;

	/**
	 * use for generated link's description
	 */
	protected String description;

	public int getPagePushOne() {
		return ++getPage;
	}

	public String getCrawlerId() {
		return crawlerId;
	}

	public void setCrawlerId(String crawlerId) {
		this.crawlerId = crawlerId;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getAllDataPriority() {
		return allDataPriority;
	}

	public void setAllDataPriority(String allDataPriority) {
		this.allDataPriority = allDataPriority;
	}

	public boolean isEscape() {
		return escape;
	}

	public void setEscape(boolean escape) {
		this.escape = escape;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getRunMode() {
		return runMode;
	}

	public void setRunMode(String runMode) {
		this.runMode = runMode;
	}

	public String getDateRange() {
		return dateRange;
	}

	public void setDateRange(String dateRange) {
		this.dateRange = dateRange;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getSchedulerName() {
		return schedulerName;
	}

	public void setSchedulerName(String schedulerName) {
		this.schedulerName = schedulerName;
	}

	public String getSchedulerVaule() {
		return schedulerVaule;
	}

	public void setSchedulerVaule(String schedulerVaule) {
		this.schedulerVaule = schedulerVaule;
	}

	public boolean isValidateRobots() {
		return validateRobots;
	}

	public void setValidateRobots(boolean validateRobots) {
		this.validateRobots = validateRobots;
	}

	public int getMaxLinkDepth() {
		return maxLinkDepth;
	}

	public void setMaxLinkDepth(int maxLinkDepth) {
		this.maxLinkDepth = maxLinkDepth;
	}

	public String getAcceptPattern() {
		return acceptPattern;
	}

	public void setAcceptPattern(String acceptPattern) {
		this.acceptPattern = acceptPattern;
	}

	public String getDenyPattern() {
		return denyPattern;
	}

	public void setDenyPattern(String denyPattern) {
		this.denyPattern = denyPattern;
	}

	public String getIsOnlyTotalNum() {
		return isOnlyTotalNum;
	}

	public void setIsOnlyTotalNum(String isOnlyTotalNum) {
		this.isOnlyTotalNum = isOnlyTotalNum;
	}

	public int getLinkDepth() {
		return linkDepth;
	}

	public void setLinkDepth(int linkDepth) {
		this.linkDepth = linkDepth;
	}

	public boolean isStop() {
		return isStop;
	}

	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
