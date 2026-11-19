package com.nagarro.jma.model;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
@JsonIgnoreProperties({"location","email","coordinates","timezone","login","registered","phone","picture","info","cell","id"})
public class User {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long userId;
 private String name; 
 private String gender;
 private String nat;
private Integer age;

private Date dob;

private String verificationstatus;

 @CreationTimestamp
 private Date dateCreated;

 @UpdateTimestamp
 private Date dateModified;

public Long getUserId() {
	return userId;
}

public void setUserId(Long userId) {
	this.userId = userId;
}


public Date getDob() {
	return dob;
}

public void setDob(Date dob) {
	this.dob = dob;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}


public String getGender() {
	return gender;
}

public void setGender(String gender) {
	this.gender = gender;
}



public Integer getAge() {
	return age;
}

public void setAge(Integer age) {
	this.age = age;
}


public String getVerificationstatus() {
	return verificationstatus;
}

public void setVerificationstatus(String verificationstatus) {
	this.verificationstatus = verificationstatus;
}

public String getNat() {
	return nat;
}

public void setNat(String nat) {
	this.nat = nat;
}

public Date getDateCreated() {
	return dateCreated;
}

public void setDateCreated(Date dateCreated) {
	this.dateCreated = dateCreated;
}

public Date getDateModified() {
	return dateModified;
}

public void setDateModified(Date dateModified) {
	this.dateModified = dateModified;
}



@Override
public String toString() {
	return "User [userId=" + userId + ", name=" + name + ", gender=" + gender + ", nat=" + nat + ", age=" + age
			+ ", dob=" + dob + ", verificationstatus=" + verificationstatus + ", dateCreated=" + dateCreated
			+ ", dateModified=" + dateModified + "]";
}

public User() {
	super();
}

public User(Long userId, String name, String gender, String nat, Integer age, Date dob, String verificationstatus,
		Date dateCreated, Date dateModified) {
	super();
	this.userId = userId;
	this.name = name;
	this.gender = gender;
	this.nat = nat;
	this.age = age;
	this.dob = dob;
	this.verificationstatus = verificationstatus;
	this.dateCreated = dateCreated;
	this.dateModified = dateModified;
}



 
}


