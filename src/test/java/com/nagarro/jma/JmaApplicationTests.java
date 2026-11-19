package com.nagarro.jma;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;
import com.nagarro.jma.controller.UserController;
import com.nagarro.jma.service.UserServiceImp;
import com.nagarro.jma.model.*;

@AutoConfigureMockMvc
@SpringBootTest
class JmaApplicationTests {

	
	@Mock
	private UserServiceImp service;
	
	@InjectMocks
	private UserController userController;


	@Autowired
	private MockMvc mockMvc;
	
	@InjectMocks
	UserController controller;
	User user1 = new User(1l, "Yashi Shukla" ,"female", "IN", 21, new Date(), "VERIFIED", new Date(),new Date());
	
	User user2 = new User(1l, "Arnav " ,"male", "US", 42, new Date(), "TO_BE_VERIFIED", new Date(),new Date());
	User user3 = new User(1l, "Lexi" ,"female", "IN", 33, new Date(), "VERIFIED", new Date(),new Date());

//runs before each test
	@BeforeEach
	public void setUp() {
		//initializes annotated fields 
		MockitoAnnotations.openMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}
	
	@Test
	public void getTest_success() throws Exception{
		ArrayList<User> list = new ArrayList<User>(Arrays.asList(user2, user1, user3));
		System.out.println(list.toString());
		Map<String, Object> map  = new HashMap<String,Object>();
		
		map.put("Data", list);
		Mockito.when(service. getUsers("age","even",2,1)).thenReturn(map);
		
		//model http req n give the expected results
		mockMvc.perform(MockMvcRequestBuilders
				.get("/users?sortType=age&sortOrder=even&limit=2&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$[\"Data\"].[0].age",equalTo(42)));
		
	}
	
	@Test
	public void getTest_fail() throws Exception{
		Map<String, Object> map  = new HashMap<String,Object>();
		map.put("statuscode", 400);
		Mockito.when(service.getUsers("age","ev",2,1)).thenReturn(map);
		mockMvc.perform(MockMvcRequestBuilders
				.get("/users?sortType=age&sortOrder=ev&limit=2&offset=1")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
//will give bad request error
		.andExpect(MockMvcResultMatchers.jsonPath("$[\"Statuscode\"]",equalTo(400)));
		
	}
	
	
	
	@Test
	public void createTest_success() throws Exception{
		List<User> list  = new ArrayList<User>();
		list.add(user1);
		list.add(user2);
//		Map<String, Object> map  = new HashMap<String,Object>();
//		map.put("data", list);
		Mockito.when(service.createUsers(2)).thenReturn(list);
		mockMvc.perform(MockMvcRequestBuilders
				.post("/users?size=2")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$[\"Data\"].[0].age",equalTo(21)));
		
	}
	
	@Test
	public void createTest_fail() throws Exception{
		List<User> list  = new ArrayList<User>();		
		Mockito.when(service.createUsers(7)).thenReturn(list);
		mockMvc.perform(MockMvcRequestBuilders
				.post("/users?size=7")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(MockMvcResultMatchers.jsonPath("$[\"Statuscode\"]",equalTo(400)));
		
	}
	
	
}


