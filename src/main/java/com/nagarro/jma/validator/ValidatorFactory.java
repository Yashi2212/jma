package com.nagarro.jma.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
 
 
@Component
public class ValidatorFactory {
 
    private final NumericValidator numericValidator;
    private final EnglishAlphabetsValidator englishAlphabetsValidator;
 
    @Autowired
    public ValidatorFactory(NumericValidator numericValidator,
    		EnglishAlphabetsValidator englishAlphabetsValidator) {
        this.numericValidator = numericValidator;
        this.englishAlphabetsValidator = englishAlphabetsValidator;
    }
 
    public Validator getValidator(String validator) {
        if (validator.equalsIgnoreCase("string") ) {
            return englishAlphabetsValidator;
        } else if (validator.equalsIgnoreCase("number")) {
            return numericValidator;
        } else {
            throw new IllegalArgumentException("Unsupported parameter type for validation");
        }
    }
}
 