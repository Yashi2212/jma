package com.nagarro.jma.controller.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
//defines the global element that are applicable to all controller class 
@ControllerAdvice
public class ControllerExceptionHandler {
//to handle exceptions witin a controller 
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorObject> resourceNotFoundException(MethodArgumentNotValidException e){
		ErrorObject message =new ErrorObject(
				HttpStatus.NOT_FOUND.value(),
				new Date(), 
				e.getMessage());
		return new ResponseEntity<ErrorObject>(message,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorObject> globalExceptionHandler(Exception e){
		ErrorObject message=new ErrorObject(HttpStatus.INTERNAL_SERVER_ERROR.value(),new Date(), e.getMessage());
		return new ResponseEntity<>(message,HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
}
