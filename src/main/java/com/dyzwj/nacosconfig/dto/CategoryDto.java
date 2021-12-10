package com.dyzwj.nacosconfig.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoryDto {

    private Integer id;

    private String cateName;

    private Integer cateParentId;

    private String document;

    private List<CategoryDto> children;

}
