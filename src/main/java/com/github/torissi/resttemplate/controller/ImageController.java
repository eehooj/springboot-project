package com.github.torissi.resttemplate.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
public class ImageController {

    @PostMapping("/image")
    public void image(MultipartHttpServletRequest mp) {
        System.out.println();
    }
    //포스트맨으로 아주 손쉽게 전송이 가능

    @PostMapping("/file")
    public void file(@RequestParam MultipartFile mp) {
        System.out.println();
    }
    //400 Bad Request ㅠㅠㅠㅠ => 둘의 차이점이 뭘까 자기 전 고민해보기
}
