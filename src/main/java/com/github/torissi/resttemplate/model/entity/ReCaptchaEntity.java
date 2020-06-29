package com.github.torissi.resttemplate.model.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Setter
public class ReCaptchaEntity {

    private Long recapId;

    private Boolean success;

    private String challenge_ts;

    private String action;

    private String hostname;

    private Float score;
}
