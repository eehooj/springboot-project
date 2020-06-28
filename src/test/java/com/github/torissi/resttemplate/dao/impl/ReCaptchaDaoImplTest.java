package com.github.torissi.resttemplate.dao.impl;

import com.github.torissi.resttemplate.dao.ReCaptchaDao;
import com.github.torissi.resttemplate.model.entity.ReCaptchaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class ReCaptchaDaoImplTest {

    @Autowired
    private ReCaptchaDaoImpl reCaptchaDaoImpl;

    @Test
    public void findAll() throws SQLException {
        List<ReCaptchaEntity> list = reCaptchaDaoImpl.findAll();

        ReCaptchaEntity entity = list.get(0);
        assertThat(entity.getScore()).isEqualTo(0.9);

    }
}