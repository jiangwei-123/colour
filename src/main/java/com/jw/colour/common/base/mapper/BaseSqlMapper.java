package com.jw.colour.common.base.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import java.util.LinkedHashMap;
import java.util.List;

@Mapper
@Repository
public interface BaseSqlMapper {
    @Select("${sql}")
    List<LinkedHashMap<String, Object>> select(String sql);

    @Insert("${sql}")
    int insert(String sql);

    @Update("${sql}")
    int update(String sql);

    @Delete("${sql}")
    int delete(String sql);
}
