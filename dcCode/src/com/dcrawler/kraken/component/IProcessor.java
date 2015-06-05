package com.dcrawler.kraken.component;

import com.dcrawler.kraken.component.entry.DefaultTask;

public interface IProcessor {
	void process(DefaultTask task) throws Exception;
}
