package com.dyzwj.nacosconfig.controller;


import com.dyzwj.nacosconfig.config.UserProperties;
import com.dyzwj.nacosconfig.util.Word2HtmlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RefreshScope
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserProperties userProperties;

    @Autowired
    Word2HtmlUtil word2HtmlUtil;


    @Value("${user.str}")
    private String str;

    @GetMapping("/get")
    public String get(){
        System.out.println(userProperties.getClass().getName());
        System.out.println(str);
        return userProperties.toString();
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }
        String s = null;
        try {
            s = word2HtmlUtil.upload(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(s);
        return "上传失败！";
    }



}
