package com.dyzwj.nacosconfig.controller;

import com.dyzwj.nacosconfig.dto.CategoryDto;
import com.dyzwj.nacosconfig.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/query/{name}")
    public List<CategoryDto> get(@PathVariable("name") String name){
        return categoryService.selectDynamic(name);
    }

    @GetMapping("/queryAll")
    public List<CategoryDto> get(){
        return categoryService.queryAll();
    }






}
