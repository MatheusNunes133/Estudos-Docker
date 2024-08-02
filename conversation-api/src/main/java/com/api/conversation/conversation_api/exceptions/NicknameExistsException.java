package com.api.conversation.conversation_api.exceptions;

public class NicknameExistsException extends Exception{
    public NicknameExistsException(String message){
        super(message);
    }
}
