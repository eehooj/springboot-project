package com.github.torissi.resttemplate.service;

import com.github.torissi.resttemplate.model.response.ReCaptchaResponse;

public interface CaptchaService {


    ReCaptchaResponse reCaptchaDecision(String token);
}
