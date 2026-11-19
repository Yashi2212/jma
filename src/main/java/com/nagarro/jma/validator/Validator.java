package com.nagarro.jma.validator;

public interface Validator<T> {
    void validate(T value);
}