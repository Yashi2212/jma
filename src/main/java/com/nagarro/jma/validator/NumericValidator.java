package com.nagarro.jma.validator;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.nagarro.jma.exception.InvalidNameException;
import com.nagarro.jma.exception.InvalidNumericException;

@Component
public class NumericValidator implements Validator<Object> {

	@Override
	public void validate(Object value) throws InvalidNumericException {	

		if(value instanceof Number) {
		String input =(String) value;
		if(!input.matches("0\\d+")) {
			throw new InvalidNameException(value+" is invalid Numeric Character ");	
	}
	}
	}

  
}