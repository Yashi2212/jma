package com.nagarro.jma.controller;

import com.nagarro.jma.controller.exceptions.ErrorObject;
import com.nagarro.jma.model.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nagarro.jma.model.User;
import com.nagarro.jma.service.UserService;
import com.nagarro.jma.util.PageInfo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	private static final int MAX_SIZE_LIMIT = 5;

	@PostMapping
	public ResponseEntity<Map<String, Object>> createUser(@RequestParam int size) {
		Map<String, Object> errorResponse = new HashMap<>();
		if (size <= 0 || size > MAX_SIZE_LIMIT) {
			errorResponse.put("Message", "Invalid Size Value , size should be between 1 and 5");
			errorResponse.put("Statuscode",HttpStatus.BAD_REQUEST.value());
			errorResponse.put("Timestamp", new Date());
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		List<User> users = userService.createUsers(size);
		Map<String, Object> map= new HashMap<>(); 
		map.put("Data",users );
		
		return new ResponseEntity<>(map, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<Map<String, Object>> getUsers(@RequestParam String sortType, @RequestParam String sortOrder,
			@RequestParam @Min(1) @Max(5) int limit, @RequestParam @Min(1) @Max(5) int offset) {
		Map<String, Object> errorResponse = new HashMap<>();
		if (!sortType.equals("name") && !sortType.equals("age")) {
			errorResponse.put("Message", "Invalid sortType. Allowed values are 'name' or 'age'");
			errorResponse.put("Statuscode", HttpStatus.BAD_REQUEST.value());
			errorResponse.put("Timestamp", new Date());
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		if (!sortOrder.equals("even") && !sortOrder.equals("odd")) {
			errorResponse.put("Message", "Invalid sortOrder. Allowed values are 'even' or 'odd'.");
			errorResponse.put("Statuscode", HttpStatus.BAD_REQUEST.value());
			errorResponse.put("Timestamp", new Date());
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		
		Map<String, Object> map;
		
		try {
            map = userService.getUsers(sortType, sortOrder, limit, offset);
        } catch (IllegalArgumentException e) {
            errorResponse.put("Statuscode", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("Message", "Invalid ENDPOINT PARAMETER");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            errorResponse.put("Statuscode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("Message", "Internal Server Error");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
		
//		Map<String, Object> map = userService.getUsers(sortType, sortOrder, limit, offset);

		return new ResponseEntity<>(map, HttpStatus.OK);
	}
}