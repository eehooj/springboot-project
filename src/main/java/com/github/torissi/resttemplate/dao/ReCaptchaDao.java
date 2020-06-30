package com.github.torissi.resttemplate.dao;

import com.github.torissi.resttemplate.model.entity.ReCaptchaEntity;
import com.github.torissi.resttemplate.model.response.ReCaptchaResponse;

import java.sql.SQLException;
import java.util.List;

public interface ReCaptchaDao {

    List<ReCaptchaEntity> findAll() throws SQLException;
    void insertReCaptcha(ReCaptchaEntity reCaptchaEntity) throws SQLException, Exception;
    void insertBulkReCaptcha(ReCaptchaEntity reCaptchaEntity) throws SQLException;

}
