package com.sky.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sky.entity.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 员工信息 Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2024-04-29
 */
public interface EmployeeMapper extends BaseMapper<Employee> {
    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    IPage<Employee> selectPage(IPage<?> page, @Param("name") String name);
}
