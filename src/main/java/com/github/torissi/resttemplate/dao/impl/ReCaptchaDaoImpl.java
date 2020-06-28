package com.github.torissi.resttemplate.dao.impl;

import com.github.torissi.resttemplate.dao.ReCaptchaDao;
import com.github.torissi.resttemplate.model.entity.ReCaptchaEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReCaptchaDaoImpl implements ReCaptchaDao {

    private static Logger logger = LoggerFactory.getLogger(ReCaptchaDaoImpl.class);

    private DataSource dataSource;

    public ReCaptchaDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<ReCaptchaEntity> findAll() throws SQLException {
        List<ReCaptchaEntity> reCaptchaEntityList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection(); //커넥션 연결
            preparedStatement = connection.prepareStatement("SELECT * FROM re_captcha_entity");
            ResultSet rs = preparedStatement.executeQuery(); //sql문을 실행시키고, 그 결과를 resultSet에 담는다

            logger.info(connection.getMetaData().getURL());
            logger.info(connection.getMetaData().getUserName());
            logger.info(connection.getMetaData().getDriverName());

            while (rs.next()) { //
                ReCaptchaEntity re = new ReCaptchaEntity();
                re.setScore(rs.getFloat("score"));
                re.setHostname(rs.getString("hostname"));
                re.setAction(rs.getString("action"));
                re.setSuccess(rs.getBoolean("success"));
                re.setChallenge_ts(rs.getString("challenge_ts"));
                re.setRecapId(rs.getLong("recap_id"));
                reCaptchaEntityList.add(re);
            }
            preparedStatement.close(); // 모든 정보를 옮겨 담으면 preparestatement 종류
        } catch (SQLException e) {
            logger.error("select 실행 중 문제 발생!", e);
        } finally {
            connection.close(); //커넥션 종료
        }

        return reCaptchaEntityList;
    }

    @Override
    public void insertReCaptcha(ReCaptchaEntity reCaptchaEntity) throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = dataSource.getConnection();
            ps = connection.prepareStatement(
                    "insert into re_captcha_entity (success, challenge_ts, action, hostname, score)"
                    + " values(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS
            );
            ps.setBoolean(1, reCaptchaEntity.getSuccess()); // 첫번째 ?에 값을 넣어줘
            ps.setString(2, reCaptchaEntity.getChallenge_ts());
            ps.setString(3, reCaptchaEntity.getAction());
            ps.setString(4, reCaptchaEntity.getHostname());
            ps.setFloat(5, reCaptchaEntity.getScore());
            ps.execute();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                reCaptchaEntity.setRecapId(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            logger.error("INSERT 실행 중 문제 발생!", e);
        } finally {
            connection.close();
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
*
* */