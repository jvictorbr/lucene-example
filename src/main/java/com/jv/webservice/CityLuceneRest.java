package com.jv.webservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jv.console.LuceneIndexManager;
import com.jv.console.impl.city.City;

@RestController
@RequestMapping("/city/query")
public class CityLuceneRest {
	
	@Autowired
	@Qualifier("activeIndexBuilder")
	private LuceneIndexManager<City> searcher;
	
	@RequestMapping(produces=MediaType.APPLICATION_JSON_VALUE, method=RequestMethod.GET)
	public ResponseEntity<List<City>> query(@RequestParam("q") String queryStr) throws Exception { 
		
		List<City> cities = searcher.search(queryStr);
		return new ResponseEntity<List<City>>(cities, HttpStatus.OK);
		
		
	}

}
