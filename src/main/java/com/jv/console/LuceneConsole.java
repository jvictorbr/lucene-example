package com.jv.console;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import com.jv.console.impl.city.City;

public class LuceneConsole {

	private static final transient Logger log = Logger.getLogger(LuceneConsole.class);

	private static final String FILENAME = "cidades2.csv";

	private Directory directory = new RAMDirectory();
	private Analyzer analyzer = new StandardAnalyzer();

	public static void main(String[] args) throws Exception {
		LuceneConsole luceneConsole = new LuceneConsole();
		luceneConsole.start();
	}
	
	public void start() throws Exception { 
		List<City> cities = readFile(FILENAME);
		createIndex(cities);
		
		String input = null;
		
		try (Scanner in = new Scanner(System.in); DirectoryReader ireader = DirectoryReader.open(directory);) {
			while ((input = in.nextLine()) != "bye") {
				IndexSearcher isearcher = new IndexSearcher(ireader);				
				QueryParser parser = new QueryParser("city", analyzer);
				Query query = parser.parse(input);
				ScoreDoc[] hits = isearcher.search(query, 50).scoreDocs;
				for (int i = 0; i < hits.length; i++) {
					Document hitDoc = isearcher.doc(hits[i].doc);
					log.debug(hitDoc.getField("city").stringValue() + "-" + hitDoc.getField("state").stringValue() + " - " + hitDoc.getField("population").stringValue());
				}				
			}
		} finally { 
			directory.close();
		}

	}
	

	private Document createDocument(City city) {
		Document doc = new Document();
		doc.add(new Field("city", city.getName(), TextField.TYPE_STORED));
		doc.add(new Field("state", city.getState(), TextField.TYPE_STORED));
		doc.add(new LongField("population", city.getPopulation(), Store.YES));
		return doc;
	}

	private void createIndex(List<City> cities) throws IOException {

		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter iwriter = new IndexWriter(directory, config);
		try {
			for (City city : cities) {
				iwriter.addDocument(createDocument(city));
			}
		} finally {
			log.debug(String.format("Created index with %s documents", cities.size()));
			iwriter.close();
		}

	}

	private List<City> readFile(String fileName) throws IOException {

		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		Reader reader = new InputStreamReader(is);

		List<City> cities = new ArrayList<>();
		Iterable<CSVRecord> csvRecords = CSVFormat.DEFAULT.parse(reader);
		for (CSVRecord csvRecord : csvRecords) { 
			
			String state = csvRecord.get(0);
			String name = csvRecord.get(3);
			String _population = csvRecord.get(4);
			Long population = Long.parseLong(_population.replaceAll(",",""));
			
			log.debug(String.format("City: %s, State: %s, Population: %s", name, state, _population));
			cities.add(new City(name, state, population));
		}
		
		return cities;
	}


}
