package com.jw.colour.models.employee.dao;


import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface EmployeeMapper {
    @Delete("delete from t_company_user where id = #{id}")
    int deleteSysRoleById(String id);

    @Update("update t_company_user set free_net_id = #{netId} where id = #{id}")
    int update(String netId,String id);
}
