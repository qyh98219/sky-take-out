package com.sky.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
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

    /**
     * 分页查询员工列表
     * @param page
     * @param name
     * @return
     */
    IPage<Employee> selectPage(IPage<?> page, String name);
}
