package com.nagarro.jma.strategy;

import java.util.List;

import com.nagarro.jma.model.User;


public interface SortStrategy {
    List<User> sort(List<User> list, String sortBy);
}