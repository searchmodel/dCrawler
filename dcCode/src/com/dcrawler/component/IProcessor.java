package com.dcrawler.component;

import com.dcrawler.component.entry.DefaultTask;

/**
 * 接口开发
 * @author Bin Xu
 *
 */
public interface IProcessor {
	void process(DefaultTask task) throws Exception;
}
