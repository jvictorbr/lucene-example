package com.jv.console;

import org.apache.commons.csv.CSVRecord;
import org.apache.lucene.document.Document;

public interface LuceneConsoleAdapter<T> {

	Document toDocument(T model);
	T fromDocument(Document document);
	T fromCSV(CSVRecord csvRecord);
	
}
