package com.github.torissi.resttemplate.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@ToString
@Getter
@Setter
public class ReCaptchaResponse implements Serializable {

    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("challenge_ts")
    private String challengeTs;
    @JsonProperty("action")
    private String action;
    @JsonProperty("hostname")
    private String hostname;
    @JsonProperty("score")
    private Float score;
    @JsonProperty("error-codes")
    protected List<String> errorCodes;


}
