package com.dyzwj.nacosconfig.mapper;

import com.dyzwj.nacosconfig.entity.CategoryPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


import java.util.List;

@Mapper
public interface CategoryMapper {

    List<CategoryPO> selectDynamic(@Param("name") String name);


    @Select("select * from cate_info")
    List<CategoryPO> queryAll();

}
