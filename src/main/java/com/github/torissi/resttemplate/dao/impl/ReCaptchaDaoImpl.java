package com.github.torissi.resttemplate.dao.impl;

import com.github.torissi.resttemplate.dao.ReCaptchaDao;
import com.github.torissi.resttemplate.model.entity.ReCaptchaEntity;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class ReCaptchaDaoImpl implements ReCaptchaDao {


    //@Slf4j이면 선언안해도됨
//   private static Logger logger = LoggerFactory.getLogger(ReCaptchaDaoImpl.class);

    private static String SELECT_ALL_SQL = "SELECT * FROM re_captcha_entity";

    private static String INSERT_SQL = "insert into re_captcha_entity (success, challenge_ts, action, hostname, score) values(?, ?, ?, ?, ?)";

    private Connection getConnection() throws Exception {
        return DriverManager.getConnection(
                // 실제 자바 프로그램과 데이터베이스를 네트워크상에서 연결해주는 메소드
                // 연결에 성공하면 DB와 연결된 상태를 Connection 객체로 표현하여 반환
                "jdbc:mysql://totoku103.iptime.org:33306/spring_study?serverTimezone=UTC&characterEncoding=UTF-8",
                "test-totoku",
                "totoku103"
        );
    }

    @Override
    public List<ReCaptchaEntity> findAll() {
        List<ReCaptchaEntity> reCaptchaEntityList = new ArrayList<>();

        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_SQL);
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
            log.error("select 실행 중 문제 발생!", e);
        }
        return reCaptchaEntityList;
    }

    @Override
    public void insertReCaptcha(ReCaptchaEntity reCaptchaEntity) {
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL);
        ) {
            setStatement(preparedStatement, reCaptchaEntity);
            preparedStatement.execute(); //쿼리 실행
        } catch (Exception e) {
            log.error("단일 INSERT 실행 중 문제 발생!", e);
        }
    }

    @Override
    public void insertBulkReCaptcha(List<ReCaptchaEntity> entityList) { //list
        try (
                Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL);
        ) {
            int count = 0;
            for (ReCaptchaEntity s : entityList) {
                if (count == 3) {
                    throw new RuntimeException();
                }

                setStatement(preparedStatement, s);
                preparedStatement.addBatch();

                count++;
                if(count % 1000 == 0){
                    // addbatch가 많이 쌓이면 ooe(Out Of Memory Exception) 발생
                    preparedStatement.executeBatch();
                }
            }
            // 잔여 데이터 처리
            preparedStatement.executeBatch();
        } catch (Exception e) {
            log.error("대량 INSERT 실행 중 문제 발생!", e);
        }
    }

    public void setStatement(PreparedStatement statement, ReCaptchaEntity entity) throws SQLException {
        statement.setBoolean(1, entity.getSuccess()); // 첫번째 ?에 값을 넣어줘
        statement.setString(2, entity.getChallenge_ts());
        statement.setString(3, entity.getAction());
        statement.setString(4, entity.getHostname());
        statement.setFloat(5, entity.getScore());
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
 * try with resource
 *  - try(...)에서 선언된 객체들에 대해서 try가 종료될 때 자동으로 자원을 해제
 *  - try에서 선언된 객체가 AutoCloseable을 구현하였다면 Java는 try구문이 종료될 때 객체의 close() 메소드를 호출
 *  - 코드를 짧고 간결하게 만들어 읽기 쉽고 유지보수가 쉬움
 *  - close를 호출하려면 if와 try-catch 구문이 많아짐 => close()를 실수로 안적을 수 있음..(내가 실제 그럼..ㅎ_
 *  - AutoCloseable은 자바7 부터 지원하기 시작한 interface
 *  - 내가 만든 자원을 try resource로 사용하고 싶으면 AutoCloseable을 implements하면 됨
 *
 * call by value
 *  - 메소드 호출 시 사용되는 인자의 메모리에 저장되어 있는 값을 복사하여 전달
 *  - 자바는 call by value
 *
 * call by reference
 *  - 메소드 호출 시 사용되는 인자 값의 메모리 주소를 복사하여 전달
 *  - 객체의 참조 값을 직접 바꾸는 것이 아니라 그 객체의 멤버변수에 접근(getter, setter)하여 그 값을 바꾸면 call by reference
 *
 * connection pool lifecycle
 *  - 어플리케이션 데이터 레이어는 데이터소스 커넥션을 위해 datasource에 요청함
 *  - datasource는 database driver를 이용하여 데이터베이스 커넥션을 열음
 *  - 데이터베이스 연결이 생성되고, tcp 소켓이 열림
 *  - 어플리케이션은 데이터 배이스를 읽고 씀
 *  - 커넥션이 더이상 요청되지 않으면 닫힘
 *  - 소켓이 닫힘
 *
 * */