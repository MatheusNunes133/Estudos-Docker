package com.api.conversation.conversation_api.exceptions;

public class PasswordNotMatchException extends Exception{
    public PasswordNotMatchException(String message){
        super(message);
    }
}
