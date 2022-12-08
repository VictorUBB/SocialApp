package com.example.lab_gui.validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}