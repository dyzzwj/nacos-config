package com.dyzwj.nacosconfig.service;

import com.dyzwj.nacosconfig.dto.CategoryDto;

import java.util.List;

public interface CategoryService {


    List<CategoryDto> selectDynamic(String name);

    List<CategoryDto> queryAll();
}
