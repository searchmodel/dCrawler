package com.dcrawler.kraken.component.entry;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * TemplateTask 完成
 * 
 * @author Bin Xu
 *
 */
public class TemplateTask extends AbstractTask {

	private static Log LOG = LogFactory.getLog(TemplateTask.class);

	protected String keywords;
	protected String tags;
	protected String priorities;
	protected String maxPages;
	protected String brands;

	protected JSONObject pageTemplate;

	private static final Pattern pattern = Pattern.compile("\\{x\\}");

	/**
	 * 格式化
	 * 
	 * @param template
	 *            eg: http://baidu.com?k={x}&b={x}
	 * @param values
	 * @return
	 */
	protected static String format(String template, String... values) {
		StringBuilder builder = new StringBuilder(template.length());
		Matcher matcher = pattern.matcher(template);
		int i = 0;
		int valueIndex = 0;
		while (i < template.length()) {
			if (matcher.find(i)) {
				String substring = template.substring(i, matcher.start());
				builder.append(substring);
				builder.append(values[valueIndex]);
				valueIndex++;
				i = matcher.end();
			} else {
				break;
			}
		}
		builder.append(template.substring(i));

		return builder.toString();
	}

	/**
	 * 编译 TemplateTask 生成多个DefaultTask
	 * 
	 * @return
	 */
	public List<DefaultTask> getGenetatedTasks() {

		if (getKeywords() == null || getKeywords().trim().equalsIgnoreCase("")) {
			LOG.warn(String
					.format("TemplateTask[%s] with uri[%s] has null or empty keywords[%s]",
							getCrawlerId(), getUri(), getKeywords()));
			setKeywords("");
		}

		String[] keywordsArray = getKeywords().split(Pattern.quote("|"));
		String[] tagsArray = null;
		String[] prioritiesArray = null;
		String[] maxPagesArray = null;
		String[] brandsArray = null;

		if (getTags() != null && !getTags().trim().equalsIgnoreCase("")) {
			tagsArray = getTags().split(Pattern.quote("|"));
		}
		if (getPriorities() != null
				&& !getPriorities().trim().equalsIgnoreCase("")) {
			prioritiesArray = getPriorities().split(Pattern.quote("|"));
		}
		if (getMaxPages() != null && !getMaxPages().trim().equalsIgnoreCase("")) {
			maxPagesArray = getMaxPages().split(Pattern.quote("|"));
		}

		if (getBrands() != null && !getBrands().trim().equalsIgnoreCase("")) {
			brandsArray = getBrands().split(Pattern.quote("|"));
		}

		List<DefaultTask> list = new ArrayList<DefaultTask>(
				keywordsArray.length);

		for (int i = 0; i < keywordsArray.length; i++) {
			String keyword = keywordsArray[i];
			DefaultTask task = new DefaultTask();
			task.setKeyword(keyword);

			if (tagsArray != null && tagsArray.length > 0) {
				if (tagsArray.length == keywordsArray.length) {
					task.setTag(tagsArray[i]);
				} else {
					// Only use the first value
					task.setTag(tagsArray[0]);
				}
			}
			if (prioritiesArray != null && prioritiesArray.length > 0) {
				if (prioritiesArray.length == keywordsArray.length) {
					int pri = 0;
					if (prioritiesArray[i] != null
							&& !prioritiesArray[i].trim().equalsIgnoreCase("")) {
						try {
							pri = Integer.parseInt(prioritiesArray[i]);
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}

					}
					task.setPriority(pri);
				} else {
					// Only use the first value
					int pri = 0;
					if (prioritiesArray[0] != null
							&& !prioritiesArray[0].trim().equalsIgnoreCase("")) {
						try {
							pri = Integer.parseInt(prioritiesArray[0]);
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}

					}
					task.setPriority(pri);
				}
			} else {
				task.setPriority(0);
			}

			if (maxPagesArray != null && maxPagesArray.length > 0) {
				if (maxPagesArray.length == keywordsArray.length) {
					int maxPage = 0;
					if (maxPagesArray[i] != null
							&& !maxPagesArray[i].trim().equalsIgnoreCase("")) {
						try {
							maxPage = Integer.parseInt(maxPagesArray[i]);
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}

					}
					task.setMaxPage(maxPage);
				} else {
					// Only use the first value
					int maxPage = 0;
					if (maxPagesArray[0] != null
							&& !maxPagesArray[0].trim().equalsIgnoreCase("")) {
						try {
							maxPage = Integer.parseInt(maxPagesArray[0]);
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}

					}
					task.setMaxPage(maxPage);
				}
			} else {
				task.setMaxPage(0);
			}

			if (brandsArray != null && brandsArray.length > 0) {
				if (brandsArray.length == keywordsArray.length) {
					task.setBrand(brandsArray[i]);
				} else {
					task.setBrand(brandsArray[0]);
				}
			}

			// 编码
			/*
			 * if keyword is http://www.example.com  do not encode/decode it
			 * isEscape() indicate whether the keyword need to be encoded to pass to URI
			 * Assume all keyword is not encoded in templateTask
			 */
			
			String[] values = keyword.split(",");
			if(isEscape()){				
				for(int j=0; j<values.length; j++){
					if(values[j].indexOf("http")!=-1){
						continue;
					}
					try {
						String val = values[j];
						if(!(val!=null&&val.trim().contains("/"))){
							values[j] = URLEncoder.encode(values[j],getEncoding());
						}
					} catch (UnsupportedEncodingException e) {
						try {
							values[j] = URLEncoder.encode(values[j],"UTF-8");
						} catch (UnsupportedEncodingException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
			
			String url = format(getUri(), values);
			task.setCrawlerId(getCrawlerId());
			task.setUri(url);
			task.setAllDataPriority(getAllDataPriority());
			task.setEscape(isEscape());
			task.setEncoding(getEncoding());
			task.setRunMode(getRunMode());
			task.setDateRange(getDateRange());
			task.setSiteName(getSiteName());
			task.setContentType(getContentType());
			task.setRange(getRange());
			task.setSchedulerName(getSchedulerName());
			task.setSchedulerVaule(getSchedulerVaule());
			task.setValidateRobots(isValidateRobots());
			task.setMaxLinkDepth(getMaxLinkDepth());
			task.setAcceptPattern(getAcceptPattern());
			task.setDenyPattern(getDenyPattern());
			task.setLinkDepth(getLinkDepth());
			task.setStop(isStop());
			task.setIsOnlyTotalNum(getIsOnlyTotalNum());
			
			task.setTemplateTask(this);
			
			task.setOrder(i);
			
			list.add(task);
		}
		return list;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getPriorities() {
		return priorities;
	}

	public void setPriorities(String priorities) {
		this.priorities = priorities;
	}

	public String getMaxPages() {
		return maxPages;
	}

	public void setMaxPages(String maxPages) {
		this.maxPages = maxPages;
	}

	public String getBrands() {
		return brands;
	}

	public void setBrands(String brands) {
		this.brands = brands;
	}

	public JSONObject getPageTemplate() {
		return pageTemplate;
	}

	public void setPageTemplate(JSONObject pageTemplate) {
		this.pageTemplate = pageTemplate;
	}

}
