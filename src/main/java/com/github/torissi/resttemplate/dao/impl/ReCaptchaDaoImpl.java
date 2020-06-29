package com.github.torissi.resttemplate.dao.impl;

import com.github.torissi.resttemplate.dao.ReCaptchaDao;
import com.github.torissi.resttemplate.model.entity.ReCaptchaEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

@Slf4j
@Repository
public class ReCaptchaDaoImpl implements ReCaptchaDao {

    /*private DataSource dataSource;
    public ReCaptchaDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }*/

    private Connection getConnection() throws Exception {
        return DriverManager.getConnection(
                // 실제 자바 프로그램과 데이터베이스를 네트워크상에서 연결해주는 메소드
                // 연결에 성공하면 DB와 연결된 상태를 Connection 객체로 표현하여 반환
                "jdbc:mysql://192.168.99.100:3306/spring_study?serverTimezone=UTC&characterEncoding=UTF-8",
                "root",
                "resttemplate"
        );
    }

    @Override
    public List<ReCaptchaEntity> findAll() {
        List<ReCaptchaEntity> reCaptchaEntityList = new ArrayList<>();

        try (
                Connection connection = getConnection(); //커넥션 연결
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM re_captcha_entity");
                ResultSet rs = preparedStatement.executeQuery(); //sql문을 실행시키고, 그 결과를 resultSet에 담는다
        ) {
            while (rs.next()) { //
                ReCaptchaEntity re = ReCaptchaEntity.builder()
                        .score(rs.getFloat("score"))
                        .action(rs.getString("action"))
                        .challenge_ts(rs.getString("challenge_ts"))
                        .hostname(rs.getString("hostname"))
                        .success(rs.getBoolean("success"))
                        .build();

                reCaptchaEntityList.add(re);
            }
        } catch (Exception e) {
//            logger.error("select 실행 중 문제 발생!", e);
        }
        return reCaptchaEntityList;
    }


    public List<ReCaptchaEntity> generateData(int loop) {
        List<ReCaptchaEntity> list = new ArrayList<>();

        final IntStream intStream = IntStream.range(0, loop);
        intStream.forEach(a -> {
            list.add(ReCaptchaEntity.builder()
                    .action(UUID.randomUUID().toString())
                    .challenge_ts(UUID.randomUUID().toString())
                    .hostname(UUID.randomUUID().toString())
                    .score(new Random().nextFloat())
                    .build());
        });
        return list;
    }

    @Override
    public void insertReCaptcha(ReCaptchaEntity reCaptchaEntity) throws SQLException {
        try (
                Connection connection = getConnection(); //커넥션 연결
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "insert into re_captcha_entity (success, challenge_ts, action, hostname, score)"
                                + " values(?, ?, ?, ?, ?)"
                );
        ) {
            preparedStatement.setBoolean(1, reCaptchaEntity.getSuccess()); // 첫번째 ?에 값을 넣어줘
            preparedStatement.setString(2, reCaptchaEntity.getChallenge_ts());
            preparedStatement.setString(3, reCaptchaEntity.getAction());
            preparedStatement.setString(4, reCaptchaEntity.getHostname());
            preparedStatement.setFloat(5, reCaptchaEntity.getScore());
            preparedStatement.execute();
        } catch (Exception e) {
            //logger.error("INSERT 실행 중 문제 발생!", e);
        }
    }

    @Override
    public void insertBulkReCaptcha(ReCaptchaEntity reCaptchaEntity) throws SQLException {
        try (
                Connection connection = getConnection(); //커넥션 연결
                PreparedStatement preparedStatement = connection.prepareStatement(
                                "insert into re_captcha_entity (success, challenge_ts, action, hostname, score)"
                                    + " values(?, ?, ?, ?, ?)"
                );
        ){

            for (int i=0; i < 5; i++) {
                preparedStatement.setBoolean(1, reCaptchaEntity.getSuccess()); // 첫번째 ?에 값을 넣어줘
                preparedStatement.setString(2, reCaptchaEntity.getChallenge_ts());
                preparedStatement.setString(3, reCaptchaEntity.getAction());
                preparedStatement.setString(4, reCaptchaEntity.getHostname());
                preparedStatement.setFloat(5, reCaptchaEntity.getScore());
            }
            preparedStatement.executeBatch();

        } catch (Exception e) {
            //logger.error("INSERT 실행 중 문제 발생!", e);
        }
    }


}



/*
 * statement
 *  - executeQuery() 나 executeUpdate() 를 실행하는 시점에 파라미터로 SQL문을 전달
 *  - SQL문을 수행하는 과정에서 매번 컴파일을 하기 때문에 성능상 이슈가 있음
 *  - 쿼리 문장 분석 -> 컴파일 -> 실행 순서를 매번 거침
 *  - 반면, PreparedStatement은 처음 한 번만 세 단계를 거치고, 캐시에 담아 재사용되어 statsment보다 성능상 좋음
 *
 * DataSource object
 *  - connection 객체를 획득하기 위해 정보를 한 단계 더 추상화
 *  - DriverManager와 데이터베이스 간에 또 하나의 레이어를 추가하는 형태
 *  - connection 객체를 획득하기 위한 오브젝트가 데이터베이스에 대한 절대 정보를 필요로 하는 것을 피함
 * DriverManager class
 *  - connection 객체를 획득하기 위해 데이터베이스에 접근
 *  - connection을 만들기 위한 정보를 프로그램 내에 하드코딩함
 *  - db정보를 수정하기 위해서는 프로그램 코드를 변경해야함 => dbms에 종속적
 *
 * try resource
 *  - try(...)에서 선언된 객체들에 대해서 try가 종료될 때 자동으로 자원을 해제
 *  - try에서 선언된 객체가 AutoCloseable을 구현하였다면 Java는 try구문이 종료될 때 객체의 close() 메소드를 호출
 *  - 코드를 짧고 간결하게 만들어 읽기 쉽고 유지보수가 쉬움
 *  - close를 호출하려면 if와 try-catch 구문이 많아짐 => close()를 실수로 안적을 수 있음..(내가 실제 그럼..ㅎ_
 *  - AutoCloseable은 자바7 부터 지원하기 시작한 interface
 *  - 내가 만든 자원을 try resource로 사용하고 싶으면 AutoCloseable을 implements하면 됨
 * */