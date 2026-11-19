package com.nagarro.jma.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//specify the type of element
@Target({ElementType.PARAMETER, ElementType.FIELD})
//for how long annotation will be available 
@Retention(RetentionPolicy.RUNTIME)

//defining a new annotation 
public @interface CustomValidation {
	
}