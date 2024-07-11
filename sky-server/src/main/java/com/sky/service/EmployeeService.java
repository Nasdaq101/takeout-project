package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * employee login
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
    create new employee
    @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * employee pagination query
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * start or stop employee account
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * search employee by ID
     * @param id
     * @return
     */
    Employee getById(Long id);

    void update(EmployeeDTO employeeDTO);
}
