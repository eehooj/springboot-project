package com.github.torissi.resttemplate.service.impl;

import com.github.torissi.resttemplate.dao.ReCaptchaDao;
import com.github.torissi.resttemplate.exception.ReCaptchaException;
import com.github.torissi.resttemplate.model.entity.ReCaptchaEntity;
import com.github.torissi.resttemplate.repository.ReCaptcahRepository;
import com.github.torissi.resttemplate.service.ReCaptchaService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLException;
import java.util.List;

@Service
public class ReCaptchaServiceImpl implements ReCaptchaService {

    private String reCaptchaApi = "https://www.google.com/recaptcha/api/siteverify";

    private String secretKey = "6LceRwEVAAAAAKZNtjFAJRHa88ou871oe3Zk34K5";

    private ReCaptchaDao reCaptchaDao;

    public ReCaptchaServiceImpl(ReCaptchaDao reCaptchaDao) {
        this.reCaptchaDao = reCaptchaDao;
    }

    @Override
    public Boolean reCaptchaDecision(String token) throws Exception {
        RestTemplate restTemplate = new RestTemplate(); //restremplate 사용

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); //MultiValueMap을 지원

        MultiValueMap<String, String> mvm = new LinkedMultiValueMap<>();
        mvm.add("secret", secretKey);
        mvm.add("response", token);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(mvm, headers);

        ReCaptchaEntity reCaptchaEntity = restTemplate.postForObject(reCaptchaApi, request, ReCaptchaEntity.class);

        reCaptchaDao.insertReCaptcha(reCaptchaEntity);

        if (reCaptchaEntity.getSuccess()) {
            if (reCaptchaEntity.getScore() > 0.3) {
                return true;
            } else {
                throw new ReCaptchaException("인증에 실패하였습니다. 다시 시도해 주세요.");
            }
        } else {
            throw new ReCaptchaException("인증에 실패하였습니다. 다시 시도해 주세요.");
        }
    }

    @Override
    public List<ReCaptchaEntity> findAll() throws SQLException {
        List<ReCaptchaEntity> list = reCaptchaDao.findAll();

        return list;
    }

}

/*
* - 헤더와 바디로 구성된 HTTP 요청을 보내기 위해 HttpEntity 사용. (바디, 헤더)
  - MultiValueMap 사용하여 바디에 여러 값을 삽입 (같은 Key를 가진 파라미터 값이 여러 개일 경우)
  - postForObject를 사용하여 통신하면 response가 object로 오는데,
    반환 타입을 ReCaptchaResponse.class로 지정하였기 때문에 ReCaptchaResponse 타입으로 반환됨!
*/
