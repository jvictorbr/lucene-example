package com.jv.console;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;

public interface LuceneIndexManager<T> {
	
	void build() throws Exception;
	List<T> search(String queryStr) throws IOException, ParseException;

}
