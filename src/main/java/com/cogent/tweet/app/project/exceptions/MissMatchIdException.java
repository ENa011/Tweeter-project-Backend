package com.cogent.tweet.app.project.exceptions;

public class MissMatchIdException extends RuntimeException {
    private String message;
    public MissMatchIdException(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
