package com.jw.colour.models.employee.controller;

import com.jw.colour.common.base.mapper.BaseSqlMapper;
import com.jw.colour.models.employee.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * employee's action
 *
 * @author jw on 2021/1/12
 */
@RestController
public class EmployeeController {

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private BaseSqlMapper baseSqlMapper;

    private static Integer num = 0;

    @RequestMapping("/first")
    public String first() {

        int i = employeeMapper.update(num + "", "43f73828b20247c18d9386a17630faa1");
       // num++;
        String sql = "update t_company_user set free_net_id = '4' where id = '43f73828b20247c18d9386a17630faa1'";
        //System.out.println(i);
       baseSqlMapper.update(sql);
        return "first";
    }
}
