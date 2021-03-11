package com.jw.colour.models.employee.controller;

import com.jw.colour.models.employee.dao.EmployeeMapper;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

import static com.jw.colour.common.ecxel.ExcelUtil.downExcel;


/**
 * employee's action
 *
 * @author jw on 2021/1/12
 */
@RestController
public class EmployeeController {

    @Autowired
    private EmployeeMapper employeeMapper;

    private static Integer num=0;

    @RequestMapping("/first")
    public String first() {
        int i = employeeMapper.update(num + "", "43f73828b20247c18d9386a17630faa1");
        num++;
        System.out.println(i);
        return "first";
    }
}
