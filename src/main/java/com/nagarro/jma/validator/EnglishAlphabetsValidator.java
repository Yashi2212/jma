package com.nagarro.jma.validator;

import org.springframework.stereotype.Component;

import com.nagarro.jma.exception.InvalidNameException;


@Component
public class EnglishAlphabetsValidator implements Validator<Object> {
	@Override
	public void validate(Object value) throws InvalidNameException {
		if(value instanceof String) {
			String input =(String) value;
        if (!input.matches("[a-zA-Z\\s]+")) {
			throw new InvalidNameException("English Character : "+input+" is invalid");
		}
	}
	}
	}


	
	
	
