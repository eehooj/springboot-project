package com.github.torissi.resttemplate.service;

import com.github.torissi.resttemplate.exception.ReCaptchaException;
import com.github.torissi.resttemplate.model.entity.ReCaptchaEntity;
import com.github.torissi.resttemplate.model.response.ReCaptchaResponse;

import java.sql.SQLException;
import java.util.List;

public interface ReCaptchaService {


    Boolean reCaptchaDecision(String token) throws Exception;

    List<ReCaptchaEntity> findAll() throws SQLException;
}
