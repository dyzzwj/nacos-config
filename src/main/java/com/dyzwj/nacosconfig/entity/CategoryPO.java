package com.dyzwj.nacosconfig.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryPO {

    private Integer id;
    private String cateName;
    private Integer cateParentId;
    private String cateParentIds;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private String document;
}
