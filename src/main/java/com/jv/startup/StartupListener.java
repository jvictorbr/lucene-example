package com.jv.startup;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.jv.console.LuceneIndexManager;

@Component
public class StartupListener implements ApplicationListener<ContextRefreshedEvent>{
	
	private static final transient Logger log = Logger.getLogger(StartupListener.class);

	@Autowired
	@Qualifier("activeIndexBuilder")
	@SuppressWarnings("rawtypes")
	private LuceneIndexManager indexBuilder;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		try {
			log.debug("Application startup listener will start the lucene index creation");
			indexBuilder.build();
			log.debug("Lucene index created sucessfully");
		} catch (Exception e) {
			log.error("Failed to create Lucene index. Cause: " + e.getMessage(), e);
		}
		
	}

}
