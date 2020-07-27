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

    @PostMapping("/file")
    public void file(@RequestParam MultipartFile mp) {
        System.out.println();
    }
}
