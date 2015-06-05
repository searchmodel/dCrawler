package com.dcrawler.kraken.process;

import com.dcrawler.kraken.component.IProcessor;
import com.dcrawler.kraken.component.entry.DefaultTask;

public class DCrawlerBasicProcess implements IProcessor {

	public void process(DefaultTask task) throws Exception {

		System.out.println("我是tast.");

	}

}
