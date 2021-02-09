package com.jw.colour.models.employee.controller;

import org.apache.catalina.connector.Response;
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

    @RequestMapping("/first")
    public String first(HttpServletRequest request, HttpServletResponse response) {
        //initMap.get("local_path");
        Map<String, Object> map = new HashMap<>();
        map.put("key", "value");
        downExcel(response, map);
        return "first";
    }
}
