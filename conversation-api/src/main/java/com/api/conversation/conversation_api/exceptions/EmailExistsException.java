package com.api.conversation.conversation_api.exceptions;

public class EmailExistsException extends Exception{
    public EmailExistsException(String message){
        super(message);
    }
}
