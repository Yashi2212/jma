package com.nagarro.jma.strategy;

import java.util.List;

import com.nagarro.jma.model.User;

//- Maintains a reference to the strategy object.
//- Allows the client to switch between different strategies.
public class SortContext {
    private SortStrategy strategy;

    public SortContext(SortStrategy strategy) {
        this.strategy = strategy;
    }

    public List<User> executeStrategy(List<User> users, String sortOrder) {
        return strategy.sort(users, sortOrder);
    }
}