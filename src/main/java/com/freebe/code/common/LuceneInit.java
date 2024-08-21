package com.freebe.code.common;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.cfg.DefaultConfig;
import org.wltea.analyzer.dic.Dictionary;

/**
 * Lucene 全文搜索工具
 * @author zhengbiaoxie
 *
 */
@Component
public class LuceneInit implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Dictionary.initial(DefaultConfig.getInstance());
	}

}
