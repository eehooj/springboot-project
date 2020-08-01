package com.github.torissi.resttemplate.dao.impl;

import com.github.torissi.resttemplate.model.entity.ReCaptchaEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@Slf4j
//@Transactional // 테스트 데이터는 실제 DB에 쌓이면 안됨.
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ReCaptchaDaoImplTest {

    @Autowired
    private ReCaptchaDaoImpl1 reCaptchaDaoImpl;

    private static final int loop = 100;

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
    @MethodSource("generateBatchData")
    public void test(List<ReCaptchaEntity> entities){ // 데이터 생성 메소드를 하나로 쓸수 있음
        // 스탑워치는 테스트쪽으로 뺄것
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            int idxCnt = 0;

            for(ReCaptchaEntity entity : entities) {
                if (idxCnt == 3) {
                    throw new RuntimeException();
                }
                reCaptchaDaoImpl.insertReCaptcha(entity);
                idxCnt++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        log.error("+++++++++++++++++++++++ 단일 인서트 " + String.valueOf(stopWatch.getTotalTimeSeconds()));
        //+++++++++++++++++++++++ 단일 인서트 95.4796305  //1m 37s 300ms
    }

    @ParameterizedTest
    @MethodSource("generateBatchData")
    public void testbatch(List<ReCaptchaEntity> entities){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            reCaptchaDaoImpl.insertBulkReCaptcha(entities);
        } catch (Exception e) {
            e.printStackTrace();
        }
        stopWatch.stop();
        log.error("+++++++++++++++++++++++ 다량 인서트 " + String.valueOf(stopWatch.getTotalTimeSeconds()));
        //+++++++++++++++++++++++ 다량 인서트 10.7458575   //12s 462ms
    }

}

/*https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests*/