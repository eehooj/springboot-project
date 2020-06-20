package com.github.torissi.resttemplate.service;

import com.github.torissi.resttemplate.exception.ReCaptchaException;
import com.github.torissi.resttemplate.model.response.ReCaptchaResponse;

public interface ReCaptchaService {


    Boolean reCaptchaDecision(String token) throws ReCaptchaException;
}
