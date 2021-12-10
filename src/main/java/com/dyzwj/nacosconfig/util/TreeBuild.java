package com.dyzwj.nacosconfig.util;

import com.dyzwj.nacosconfig.dto.CategoryDto;

import java.util.ArrayList;
import java.util.List;

public class TreeBuild {

    public static List<CategoryDto> buildTree(List<CategoryDto> datas){
        List<CategoryDto> tree = new ArrayList<>();
        for (CategoryDto data : datas) {
            if(0 == data.getCateParentId()){
                tree.add(data);
            }

            CategoryDto categoryDto = datas.stream().filter(d -> data.getCateParentId().equals(d.getId())).findFirst().orElse(null);
            if(categoryDto != null){
                if(categoryDto.getChildren() == null){
                    categoryDto.setChildren(new ArrayList<>());
                }
                categoryDto.getChildren().add(data);
            }


        }
        return tree;
    }
}
