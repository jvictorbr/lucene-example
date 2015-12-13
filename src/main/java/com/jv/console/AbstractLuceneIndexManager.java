package com.jv.console;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractLuceneIndexManager<T> implements LuceneIndexManager<T> {
	
	private static final transient Logger log = Logger.getLogger(AbstractLuceneIndexManager.class);
	
	@Autowired
	private Directory directory;
	
	@Autowired
	private Analyzer analyzer;
	
	@Autowired
	private CSVFileReader fileReader;
	
	protected abstract String getFileName();
	protected abstract LuceneConsoleAdapter<T> getAdapter();
	
	public AbstractLuceneIndexManager() {
		
	}
	

	@Override
	public void build() throws Exception { 
		List<T> modelArray = readFile(getFileName());
		populateIndex(modelArray);
	}
	
	@Override
	public List<T> search(String queryStr) throws IOException, ParseException {
		
		List<T> result = new ArrayList<>();
		
		try (DirectoryReader ireader = DirectoryReader.open(directory)) { 
			
			IndexSearcher isearcher = new IndexSearcher(ireader);				
			QueryParser parser = new QueryParser("x", analyzer);
			Query query = parser.parse(queryStr);
			ScoreDoc[] hits = isearcher.search(query, 50).scoreDocs;
			for (int i = 0; i < hits.length; i++) {
				Document hitDoc = isearcher.doc(hits[i].doc);
				T model = getAdapter().fromDocument(hitDoc);
				result.add(model);
			}		
			
		}
		
		return result;
		
	}

	private void populateIndex(List<T> modelArray) throws IOException {

		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		
		try (IndexWriter iwriter = new IndexWriter(directory, config);) {
			for (T model : modelArray) {
				Document document = getAdapter().toDocument(model);
				iwriter.addDocument(document);
			}
			log.debug(String.format("Created index with %s documents", modelArray.size()));
		}

	}

	private List<T> readFile(String fileName) throws IOException {
		
		Iterable<CSVRecord> csvRecords = fileReader.readFile(fileName);
		List<T> modelArray = new ArrayList<>();
		
		for (CSVRecord csvRecord : csvRecords) { 
			T model = getAdapter().fromCSV(csvRecord);
			modelArray.add(model);
		}
		
		return modelArray;
	}

}
