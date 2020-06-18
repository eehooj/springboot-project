package com.github.torissi.resttemplate.controller;

import com.github.torissi.resttemplate.model.response.ReCaptchaResponse;
import com.github.torissi.resttemplate.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ReCaptChaController {

    @Autowired
    CaptchaService captchaService;

    @PostMapping("/captcha") //rest api
    public ResponseEntity<String> captcha(@RequestParam String token) {
        ReCaptchaResponse res;
        try {
            res = captchaService.reCaptchaDecision(token);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body("로봇임");
        }
 
        return ResponseEntity.ok("로봇 아님");
    }

}
