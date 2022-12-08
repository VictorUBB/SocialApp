package com.example.lab_gui.validators;

import com.example.lab_gui.entities.User;
//import socialnetwork.entities.Utilizator;

import java.util.Objects;

public class UtilizatorValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        //TODO: implement method validate
        if(Objects.equals(entity.getFirst_name(), "")){
            throw new ValidationException("Numele utilizatorului este null");
        }
        if(Objects.equals(entity.getLast_name(), "")){
            throw new ValidationException("Prenumele utilizatoruli este null");
        }

    }
}
