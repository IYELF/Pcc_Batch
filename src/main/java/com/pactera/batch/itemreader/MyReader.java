package com.pactera.batch.itemreader;

import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class MyReader implements ItemReader<String>{

	private Iterator<String> iterator;
	
	public MyReader(List<String> list) {
		// TODO Auto-generated constructor stub
		this.iterator=list.iterator();
	}
	
	@Override
	public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		// TODO Auto-generated method stub
		if(iterator.hasNext()) {
			return this.iterator.next();
		}else {
			return null;
		}
	}

}
