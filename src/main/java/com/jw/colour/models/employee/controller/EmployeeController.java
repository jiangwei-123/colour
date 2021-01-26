package com.jw.colour.models.employee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.jw.colour.common.listener.InitPropertiesConfig.initMap;

/**
 * employee's action
 *
 * @author jw on 2021/1/12
 */
@Controller
public class EmployeeController {

    @RequestMapping("/first")
    public String first() {
        initMap.get("local_path");
        return "first";
    }
}
