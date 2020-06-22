package com.github.torissi.resttemplate.service.impl;

import com.github.torissi.resttemplate.exception.ReCaptchaException;
import com.github.torissi.resttemplate.model.response.ReCaptchaResponse;
import com.github.torissi.resttemplate.service.ReCaptchaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ExtendWith(SpringExtension.class)
@RestClientTest(ReCaptchaService.class) //REST Client test
class CaptchaServiceImplTest {

/*    private String token = "03AGdBq24sy3gqEoKowVCK3Ul-gHR6S7nX8Sld6g-pLXLCgsn_0PKVbZ1FokOYLOvIcq9oD4TA8TkzEuvWN0uzK2dMwbnGs0andLsXmzDYPVac3sqDHD6aKWV0NEqhT0bM0yUp96qT_Avj1kD57M9BP97iTJXKkWEA3N7inFdpTnrMvRN2E-wWSsM4gb_RFBJaF6ktAnQ_a5w_OoEn-caARuzgiwOR8RwyB3qSp-8k-KaJkgqVmi6V8TLFpUNKvNBkw9f6bYw-TGy-rTnDfre4-7jVRbK4QijpBeyLrBSQr7da_6q-p-HDnc2vpHyyzX6MLU2Cgdu2ylBhZni4-SqVD8smHPEXRVwtHG0AznXHXlOYwpfn1qltUObFAxN9tePfDqs3AYzY6SOT";
    //토큰을 매 번 새로운 걸 써야하는데 어떻게 해야할 지 모르겠음

    @Autowired
    ReCaptchaService reCaptchaService;

    @Test
    public void getScore() throws ReCaptchaException {
        RestTemplate restTemplate = new RestTemplate();

        MockRestServiceServer mockRestServiceServer
                = MockRestServiceServer.bindTo(restTemplate).build();

        String expectResult = "{\"success\" : false, \"hostname\" : \"localhost\"}";
        mockRestServiceServer.expect(requestTo("/captcha"))
                            .andRespond(withSuccess(expectResult, MediaType.APPLICATION_JSON));


        Boolean reCaptchaResponse = reCaptchaService.reCaptchaDecision(token);

        assertThat(reCaptchaResponse).isEqualTo("false");
    }*/
}