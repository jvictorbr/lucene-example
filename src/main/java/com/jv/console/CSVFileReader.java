package com.jv.console;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

@Component
public class CSVFileReader {

	public Iterable<CSVRecord> readFile(String fileName) throws IOException { 
		
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		Reader reader = new InputStreamReader(is);		
		Iterable<CSVRecord> csvRecords = CSVFormat.DEFAULT.parse(reader);
		return csvRecords;
		
	}
	
}
