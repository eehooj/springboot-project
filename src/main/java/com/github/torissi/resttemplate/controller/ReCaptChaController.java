package com.github.torissi.resttemplate.controller;

import com.github.torissi.resttemplate.model.entity.ReCaptchaEntity;
import com.github.torissi.resttemplate.model.response.ResultResponse;
import com.github.torissi.resttemplate.service.ReCaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
public class ReCaptChaController {

    @Autowired
    ReCaptchaService reCaptchaService;

    @GetMapping("/")
    public String indexPage(Model model) throws SQLException {
        List<ReCaptchaEntity> entities = reCaptchaService.findAll();

        model.addAttribute("list", entities);
        return "index"; //index 페이지로 보내고 싶다,,,,,
    }

    @PostMapping("/captcha") //rest api
    public ResponseEntity<ResultResponse> captcha(@RequestParam String token) {
        ResultResponse resultResponse = new ResultResponse();
        Boolean res = false;

        try {
            res = reCaptchaService.reCaptchaDecision(token);
        } catch (Exception exception) {
            resultResponse.setMessage(exception.getMessage());
            resultResponse.setCode(400);
            return ResponseEntity.ok(resultResponse);
        }

        resultResponse.setMessage("로봇이 아닙니다.");
        resultResponse.setCode(200);
        return ResponseEntity.ok(resultResponse);
    }

}

/*
* ResponseEntity 생성자 방식 사용
* 서비스단에서 내가 만든 Exception을 발생시킴
* ResponseEntity<ResultResponse>(resultResponse, HttpStatus.OK); 라고 했는데 사실 HttpStatus.BAD_REQUEST 넣고 싶음
* 그러나 넣으면 진짜 400 에러 발생..ㅎ
* 에러 처리 너무 어려움,,,,
* */

/*피드백 내용
* 1. ResponseEntity
*   - 생성자 방식 : new 할 때 마다 heap메모리 자리를 차지함 => 메모리 효율이 비교적 좋지 않음
*   - builder : static 처럼 한 번 메모리에 올라가면 계속해서 그 정보를 사용
* 2. HttpStatus.BAD_REQUEST
*   - header에 400에러가 새겨지기 때문에 스크립트단에서 에러 발생하는 것 맞음
*   - 때문에 일단 ok로 컨트롤러 단에서 보내고 그 앞단인 interceptor 또는 filter로 파싱을 하여 원하는 값을 던지자
*   - AOP, interceptor, filter 공부 필요
* 3. webjar
*   - webjar로 js를 pom.xml로 주입을 받으면 좋은 점이 버전 관리가 용이함
*   - 만약 넣어줘야하는 라이브러리가 여러 개 인 경우 webjar를 사용하면 간단하게 주입할 수 있음 (예를 들면 bootstrap)
* 
* * 다음 미션 * *
*  - 연/월/일을 string으로 받은 후 localDate로 바꾸는 util을 만듦
*  - 그리고 그 util을 @Autowired를 통해 컨트롤러나 서비스단에 주입하기
*  - 이 작업을 하면서 bean lifecycle을 공부하자! 
* 
* * 그 다음 미션 * *
*  - properties로 설정한 것들 java config로 바꾸기
*  - springboot 공식 문서 중에서 config 설정하는 거 있음 => 찾아보기
* */