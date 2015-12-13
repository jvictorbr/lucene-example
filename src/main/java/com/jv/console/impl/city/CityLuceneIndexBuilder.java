package com.jv.console.impl.city;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jv.console.AbstractLuceneIndexManager;
import com.jv.console.LuceneConsoleAdapter;

@Component
public class CityLuceneIndexBuilder extends AbstractLuceneIndexManager<City>{
	
	private @Autowired CityAdapter cityAdapter;

	@Override
	protected String getFileName() {
		return "cidades2.csv";
	}

	@Override
	protected LuceneConsoleAdapter<City> getAdapter() {
		return cityAdapter;
	}

}
