package com.jv.startup;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;

import com.jv.console.LuceneIndexManager;
import com.jv.console.impl.city.CityLuceneIndexBuilder;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages="com.jv")
public class Startup {
	
	@Autowired
	private CityLuceneIndexBuilder indexBuilder;
	
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public Directory directory() {
		return new RAMDirectory();
	}
	
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public Analyzer analyzer() { 
		return new StandardAnalyzer();
	}
	
	@Bean
	@Qualifier("activeIndexBuilder")
	@SuppressWarnings("rawtypes")
	public LuceneIndexManager indexBuilder() {
		return indexBuilder;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Startup.class, args);
	}

}
