package com.nagarro.jma.service;


import java.util.List;
import java.util.Map;

import com.nagarro.jma.model.User;

public interface UserService {
    List<User> createUsers(int size);

   Map<String,Object> getUsers(String sortType, String sortOrder, int limit, int offset);
}