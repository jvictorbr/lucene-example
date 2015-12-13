package com.jv.console.impl.city;

import org.apache.commons.csv.CSVRecord;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.springframework.stereotype.Component;

import com.jv.console.LuceneConsoleAdapter;

@Component
public class CityAdapter implements LuceneConsoleAdapter<City> {

	@Override
	public Document toDocument(City city) {
		Document doc = new Document();
		doc.add(new Field("city", city.getName(), TextField.TYPE_STORED));
		doc.add(new Field("state", city.getState(), TextField.TYPE_STORED));
		doc.add(new LongField("population", city.getPopulation(), Store.YES));
		return doc;	
	}

	@Override
	public City fromCSV(CSVRecord csvRecord) {
		String state = csvRecord.get(0);
		String name = csvRecord.get(3);
		String _population = csvRecord.get(4);
		Long population = Long.parseLong(_population.replaceAll(",",""));
		return new City(name, state, population);
	}

	@Override
	public City fromDocument(Document document) {
		String name = document.getField("city").stringValue();
		String state = document.getField("state").stringValue();
		Long population = document.getField("population").numericValue().longValue();
		return new City(name, state, population);
	}

}
