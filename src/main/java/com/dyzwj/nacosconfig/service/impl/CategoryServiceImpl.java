package com.dyzwj.nacosconfig.service.impl;

import com.dyzwj.nacosconfig.dto.CategoryDto;
import com.dyzwj.nacosconfig.entity.CategoryPO;
import com.dyzwj.nacosconfig.mapper.CategoryMapper;
import com.dyzwj.nacosconfig.service.CategoryService;
import com.dyzwj.nacosconfig.util.TreeBuild;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> selectDynamic(String name) {
         return buildTree(categoryMapper.selectDynamic(name));
    }

    private List<CategoryDto> buildTree(List<CategoryPO> categoryPOS) {
        List<CategoryDto> categoryDtos = new ArrayList<>();

        for (CategoryPO categoryPO : categoryPOS) {
            CategoryDto categoryDto = new CategoryDto();
            BeanUtils.copyProperties(categoryPO,categoryDto);
            categoryDtos.add(categoryDto);
        }
        return TreeBuild.buildTree(categoryDtos);
    }

    @Override
    public List<CategoryDto> queryAll() {
        return buildTree(categoryMapper.queryAll());
    }
}
