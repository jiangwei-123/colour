package com.jw.colour.models.employee.service;

import com.jw.colour.models.employee.dao.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jw on 2021/2/25
 */
@Service
public class EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;


}
