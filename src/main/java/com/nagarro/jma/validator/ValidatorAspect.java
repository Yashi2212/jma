package com.nagarro.jma.validator;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nagarro.jma.exception.InvalidNameException;
import com.nagarro.jma.exception.InvalidNumericException;

@Aspect
@Component
public class ValidatorAspect {

	@Autowired
   private final ValidatorFactory validatorFactory;

   @Autowired
   public ValidatorAspect(ValidatorFactory validatorFactory) {
       this.validatorFactory = validatorFactory;
   }

   public void validateInput(@CustomValidation Object param, String type) throws InvalidNameException, InvalidNumericException {
	   Validator validator = validatorFactory.getValidator(type);
       validator.validate(String.valueOf(param));
   }
}