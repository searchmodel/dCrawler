package com.dcrawler.kraken.component;

import com.dcrawler.kraken.component.entry.DefaultTask;

/**
 * 接口开发
 * @author Bin Xu
 *
 */
public interface IProcessor {
	void process(DefaultTask task) throws Exception;
}
