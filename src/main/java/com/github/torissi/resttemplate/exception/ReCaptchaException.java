package com.github.torissi.resttemplate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ReCaptchaException extends Exception{

    public ReCaptchaException(String message) {
        super(message);
    }

}
