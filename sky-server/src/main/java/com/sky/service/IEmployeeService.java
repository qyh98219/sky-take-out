package com.sky.service;

import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 员工信息 服务类
 * </p>
 *
 * @author baomidou
 * @since 2024-04-29
 */
public interface IEmployeeService extends IService<Employee> {
    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);
}
