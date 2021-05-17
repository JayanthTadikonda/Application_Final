package com.jay.MongoDBUserCreation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AllFieldsAreNecessaryException extends RuntimeException{

    public AllFieldsAreNecessaryException(String exception){
        super(exception);
    }

}
