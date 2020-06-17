package com.github.torissi.resttemplate.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 요청 처리 결과 응답 모델.
 * Created by JinBum Jeong on 2019-03-27.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultResponse<T> {

    /**
     * 성공 시 결과 코드.
     */
    public static final int RESULT_SUCCESS_CODE = 200;

    /**
     * 실패 시 결과 코드.
     */
    public static final int RESULT_FAIL_CODE = 500;
	
    public ResultResponse() {
        // DEFAULT CONSTRUCTOR FOR FEIGN
    }

    public ResultResponse(int code) {
        this(code, null, null);
    }

    public ResultResponse(int code, String message) {
        this(code, message, null);
    }

    public ResultResponse(int code, String message, T model) {
        setCode(code);
        setMessage(message);
        setModel(model);
    }

    /**
     * 처리 결과 코드.
     */
    @JsonProperty("result")
    private int code;

    /**
     * 처리 결과 메시지.
     */
    private String message;

    /**
     * 처리 결과 모델.
     */
    private T model;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("data")
    public T getModel() {
        return model;
    }

    private void setModel(T model) {
        this.model = model;
    }


}
