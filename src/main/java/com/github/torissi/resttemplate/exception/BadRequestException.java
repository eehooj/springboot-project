package com.github.torissi.resttemplate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 잘못된 요청시 발생하는 exception.
 * Created by JinBum Jeong on 2019-03-21.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Bad Word inspector")
public class BadRequestException extends RuntimeException {

    /**
     * 생성자
     * @param message 예외 메시지
     */
    public BadRequestException(String message) {
        super(message);
    }

    /**
     * 생성자
     * @param message 예외 메시지
     * @param cause 예외 원인
     */
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

}
