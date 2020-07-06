package com.github.torissi.resttemplate.dao.impl;

import com.github.torissi.resttemplate.model.entity.ReCaptchaEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ReCaptchaDaoImplTest {

    @Autowired
    private ReCaptchaDaoImpl reCaptchaDaoImpl;

    private static final int loop = 50000;

    static Stream<ReCaptchaEntity> generateData() {
        List<ReCaptchaEntity> list = new ArrayList<>();

        final IntStream intStream = IntStream.range(0, loop);
        intStream.forEach(a -> {
            list.add(ReCaptchaEntity.builder()
                    .success(true)
                    .action(UUID.randomUUID().toString())
                    .challenge_ts(UUID.randomUUID().toString())
                    .hostname(UUID.randomUUID().toString())
                    .score(new Random().nextFloat())
                    .build());
        });
        return list.stream();
    }

    static Stream<Arguments> generateBatchData() {
        List<ReCaptchaEntity> list = new ArrayList<>();

        final IntStream intStream = IntStream.range(0, loop);
        intStream.forEach(a -> {
            list.add(ReCaptchaEntity.builder()
                    .success(true)
                    .action(UUID.randomUUID().toString())
                    .challenge_ts(UUID.randomUUID().toString())
                    .hostname(UUID.randomUUID().toString())
                    .score(new Random().nextFloat())
                    .build());
        });

        return Stream.of( arguments(list) );
    }

    @Test
    public void findAll() throws SQLException {
        List<ReCaptchaEntity> list = reCaptchaDaoImpl.findAll();

        ReCaptchaEntity entity = list.get(0);
        assertThat(entity.getSuccess()).isEqualTo(true);
    }

    @ParameterizedTest
    @MethodSource("generateData")
    public void test(ReCaptchaEntity entity){
        try {
            reCaptchaDaoImpl.insertReCaptcha(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @MethodSource("generateBatchData")
    public void testbatch(List<ReCaptchaEntity> entities){
        try {
            reCaptchaDaoImpl.insertBulkReCaptcha(entities);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

/*https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests*/