package com.github.torissi.resttemplate.controller;

import com.github.torissi.resttemplate.model.response.ReCaptchaResponse;
import com.github.torissi.resttemplate.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ReCaptChaController {

    @Autowired
    CaptchaService captchaService;

    @GetMapping("/")
    public String indexPage() {
        return "index";
    } //view resolver

    @PostMapping("/captcha") //rest api
    public @ResponseBody ResponseEntity<String> captcha(@RequestBody String token) {
        ReCaptchaResponse res;
        try {
            res = captchaService.reCaptchaDecision(token);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body("로봇임");
        }
 
        return ResponseEntity.ok("로봇 아님");
    }

}
