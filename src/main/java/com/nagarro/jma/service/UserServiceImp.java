package com.nagarro.jma.service;

import org.aspectj.lang.reflect.CatchClauseSignature;
import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;
import com.nagarro.jma.strategy.*;
import java.io.IOException;
import java.net.http.*;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import com.nagarro.jma.model.User;
import com.nagarro.jma.repo.UserRepository;
import com.nagarro.jma.strategy.AgeSorting;
import com.nagarro.jma.strategy.NameSorting;
import com.nagarro.jma.strategy.SortStrategy;
import com.nagarro.jma.validator.EnglishAlphabetsValidator;
import com.nagarro.jma.validator.ValidatorAspect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import java.util.*;
import com.nagarro.jma.util.*;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Service
public class UserServiceImp implements UserService {

	
	@Autowired
	ValidatorAspect validator;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	@Qualifier("webClientApi1")
	private WebClient.Builder webClientBuilderApi1;

	@Autowired
	@Qualifier("webClientApi2")
	private WebClient.Builder webClientBuilderApi2;

	@Autowired
	@Qualifier("webClientApi3")
	private WebClient.Builder webClientBuilderApi3;

	@Autowired
	public void UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public List<User> createUsers(int size) {
	        validator.validateInput(size, "number");
	        String rawJsonResponse = fetchRandomUsers(size);
	        if (rawJsonResponse == null || rawJsonResponse.isEmpty()) {
	            System.out.println("Warning: Raw JSON response contains 'null' values.");
	            return Collections.emptyList();
	        }
	        List<User>  users = parseJsonResponse(rawJsonResponse);
	        return saveAndVerifyUsers(users);	
	}
	
	@Override
	public Map<String,Object> getUsers(String sortType, String sortOrder, int limit, int offset) {
		System.out.println("Sort type is "+sortType+"Sort Order is: "+sortOrder);
		validator.validateInput(sortType,"string");
		validator.validateInput(sortOrder, "string");
		validator.validateInput(limit, "number");
		validator.validateInput(offset, "number");
		int total=(int) userRepository.count();
		Map<String, Object> usermap =new HashMap<String,Object>();
		try {			
			List<User> users=new ArrayList<User>(userRepository.findAllAfterOffset(offset, limit));
						
			System.out.println("count "+users.size());
			
			SortStrategy sortStrategy;
	        if ("Name".equalsIgnoreCase(sortType)) {
	            sortStrategy =new NameSorting();
	        } else if("Age".equalsIgnoreCase(sortType)) {
	            sortStrategy =new AgeSorting();
	        }
	        else {
	        	throw new IllegalArgumentException("Invalid sortType");
	        }

			List<User> sortedUsers =sortStrategy.sort(users,sortOrder);	
			HashMap<String, Object> map =new HashMap<String,Object>();
			map.put("Data", sortedUsers);
			map.put("PageInfo", getPageInfo(total, offset, limit));
			usermap=sortbykey(map);
			return usermap;
		} catch (Exception e) {
			e.printStackTrace();
			return usermap;
		}
	}
	
	 private String fetchRandomUsers(int size) {
	        return webClientBuilderApi1.build()
	                .get()
	                .uri("https://randomuser.me/api/?results=" + size)
	                .retrieve()
	                .bodyToMono(String.class)
	                .block();
	    }

	 private List<User> parseJsonResponse(String rawJsonResponse) {
	        ObjectMapper objectMapper = new ObjectMapper();
	        try {
	            JsonNode rootNode = objectMapper.readTree(rawJsonResponse);
	            JsonNode resultsNode = rootNode.get("results");

	            if (resultsNode != null && resultsNode.isArray()) {
	                return parseUserNodes(resultsNode);
	            } else {
	                System.out.println("Warning: 'results' node is not an array.");
	                return Collections.emptyList();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	            return Collections.emptyList();
	        }
	    }

	    private List<User> parseUserNodes(JsonNode resultsNode) throws IOException {
	        ObjectMapper objectMapper = new ObjectMapper();
	        List<User> users = new ArrayList<>();

	        for (JsonNode userNode : resultsNode) {
	            User user = parseUserData(objectMapper, userNode);
	            if (user != null) {
	                users.add(user);
	            }
	        }
	        return users;
	    }

	    private User parseUserData(ObjectMapper objectMapper, JsonNode userNode) throws IOException {
	        String dob = userNode.path("dob").path("date").asText();
	        int age = userNode.path("dob").path("age").asInt();
	        JsonNode nameNode = userNode.path("name");

	        if (nameNode.isObject()) {
	            String fullName = nameNode.path("title").asText() + " " +
	                    nameNode.path("first").asText() + " " +
	                    nameNode.path("last").asText();

	            ((ObjectNode) userNode).set("name", new TextNode(fullName));
	            ((ObjectNode) userNode).set("age", new IntNode(age));
	            ((ObjectNode) userNode).set("dob", new TextNode(dob));

	            User user = objectMapper.treeToValue(userNode, User.class);
	            validator.validateInput(user.getName(), "string");
	            return user;
	        }
	        return null;
	    }
	    
	private List<User> saveAndVerifyUsers(List<User> users) {
		return Flux.fromIterable(users).parallel().runOn(Schedulers.boundedElastic()).map(this::verifyAndSaveUser)
				.sequential().collectList().block();
	}

	private User verifyAndSaveUser(User user) {
		try {
			String verificationstatus = verifyUser(user);
			user.setVerificationstatus(verificationstatus);
			return userRepository.save(user);
		} catch (Exception e) {
			e.printStackTrace();
			return userRepository.save(user);

		}
	}

	 private String verifyUser(User user) {
	        try {
	            String name = user.getName();
	            String firstName = getFirstName(name);
	            List<String> nationality = getNationality(firstName);
	            String gender = getGender(firstName);

	            for (String nat : nationality) {
	                if (user.getNat().equals(nat) && user.getGender().equals(gender)) {
	                    return "VERIFIED";
	                }
	            }
	            return "TO_BE_VERIFIED";
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return "";
	    }
	 private List<String> getNationality(String firstName) {
	        String rawJsonResponse = fetchApiData(webClientBuilderApi2.baseUrl("https://api.nationalize.io"), "/?name=" + firstName);
	        if (rawJsonResponse != null && !rawJsonResponse.isEmpty() && !rawJsonResponse.contains("null")) {
	            return parseNationalityResponse(rawJsonResponse);
	        }
	        return Collections.emptyList();
	    }
	 
	 private String getGender(String firstName) {
		    String rawJsonResponse = fetchApiData(webClientBuilderApi1.baseUrl("https://api.genderize.io"), "?name=" + firstName);
		    if (rawJsonResponse != null && !rawJsonResponse.isEmpty() && !rawJsonResponse.contains("null")) {
		        return parseGenderResponse(rawJsonResponse);
		    }
		    return null;
		}
	
	private String fetchApiData(WebClient.Builder webClientBuilder, String uri) {
        try {
            return webClientBuilder.build()
                    .get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	private List<String> parseNationalityResponse(String rawJsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(rawJsonResponse);
            JsonNode resultsNode = rootNode.get("country");
            List<String> nationalityList = new ArrayList<>();
            int n = resultsNode.size();
            for (int i = 0; i < n; i++) {
                String nationality = resultsNode.get(i).path("country_id").asText();
                nationalityList.add(nationality);
            }
            return nationalityList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

 private String parseGenderResponse(String rawJsonResponse) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
        JsonNode rootNode = objectMapper.readTree(rawJsonResponse);
        return rootNode.get("gender").asText();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

	private String getFirstName(String name) {
		String[] nameparts = name.split("\\s+");
		if (nameparts.length > 1) {
			String firstname = nameparts[1];
			return firstname;
		} else {
			System.out.println("Invalid Full Name format");
		}
		return "Invalid";
	}
	
	private String removeNamePrefix(String fullName) {
	    if (fullName.startsWith("Mr ") || fullName.startsWith("Miss ") || fullName.startsWith("Mrs ")) {
	        return fullName.substring(fullName.indexOf(' ') + 1);
	    }
	    return fullName;
	}

	
	private PageInfo getPageInfo(int total, int offset, int limit) {
		
		PageInfo pageInfo= new PageInfo();
		
		pageInfo.setHasNextPage(offset + limit < total);
		pageInfo.setHasPreviousPage(offset > 0);
		pageInfo.setTotal(total);
        return pageInfo;
    }
	
	 private Map<String,Object> sortbykey(Map<String ,Object> map)
	    {
	        TreeMap<String, Object> sorted = new TreeMap<>();
	        sorted.putAll(map);	       
	        return sorted;
	    }
	}
