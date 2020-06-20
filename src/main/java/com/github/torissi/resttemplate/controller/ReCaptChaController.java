package com.github.torissi.resttemplate.controller;

import com.github.torissi.resttemplate.model.response.ResultResponse;
import com.github.torissi.resttemplate.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReCaptChaController {

    @Autowired
    CaptchaService captchaService;

    @PostMapping("/captcha") //rest api
    public ResponseEntity<ResultResponse> captcha(@RequestParam String token) {
        ResultResponse resultResponse = new ResultResponse();
        Boolean res = false;

        try {
            res = captchaService.reCaptchaDecision(token);
        } catch (Exception exception) {
            resultResponse.setMessage(exception.getMessage());
            resultResponse.setCode(400);
            return new ResponseEntity<ResultResponse>(resultResponse, HttpStatus.OK);
        }

        resultResponse.setMessage("로봇이 아닙니다.");
        resultResponse.setCode(200);
        return new ResponseEntity<>(resultResponse, HttpStatus.OK);
    }
}

/*
* ResponseEntity 생성자 방식 사용
* 서비스단에서 내가 만든 Exception을 발생시킴
* ResponseEntity<ResultResponse>(resultResponse, HttpStatus.OK); 라고 했는데 사실 HttpStatus.BAD_REQUEST 넣고 싶음
* 그러나 넣으면 진짜 400 에러 발생..ㅎ
* 에러 처리 너무 어려움,,,,
* */
