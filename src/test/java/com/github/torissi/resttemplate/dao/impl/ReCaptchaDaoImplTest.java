package com.github.torissi.resttemplate.dao.impl;

import com.github.torissi.resttemplate.model.entity.ReCaptchaEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ReCaptchaDaoImplTest {

    @Autowired
    private ReCaptchaDaoImpl reCaptchaDaoImpl;

    @Test
    public void findAll() throws SQLException {
        List<ReCaptchaEntity> list = reCaptchaDaoImpl.findAll();

        ReCaptchaEntity entity = list.get(0);
        assertThat(entity.getScore()).isEqualTo(0.9);
    }

    int loop = 100_000;

    @Test
    public void test(){
        final List<ReCaptchaEntity> entities = reCaptchaDaoImpl.generateData(loop);
        entities.stream().forEach(i -> {
            try {
                reCaptchaDaoImpl.insertReCaptcha(i);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void testbatch(){
        final List<ReCaptchaEntity> entities = reCaptchaDaoImpl.generateData(loop);
        entities.stream().forEach(i -> {
            try {
                reCaptchaDaoImpl.insertReCaptcha(i);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}