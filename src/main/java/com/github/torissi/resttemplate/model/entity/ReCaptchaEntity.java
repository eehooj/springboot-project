package com.github.torissi.resttemplate.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Entity
public class ReCaptchaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recapId;

    private Boolean success;

    private String challenge_ts;

    private String action;

    private String hostname;

    private Float score;
}
