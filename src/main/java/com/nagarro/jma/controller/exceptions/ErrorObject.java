package com.nagarro.jma.controller.exceptions;

import java.util.Date;

public class ErrorObject {
	private Object message;
	private int statuscode;
	private Date timestamp;
	public Object getMessage() {
		return message;
	}
	public void setMessage(Object message) {
		this.message = message;
	}
	public int getStatuscode() {
		return statuscode;
	}
	public void setStatuscode(int statuscode) {
		this.statuscode = statuscode;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public ErrorObject( int statuscode, Date timestamp, Object message) {
		super();
		this.message = message;
		this.statuscode = statuscode;
		this.timestamp = timestamp;
	}
	public ErrorObject() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
