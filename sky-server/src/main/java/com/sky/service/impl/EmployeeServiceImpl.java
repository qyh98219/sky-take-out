package com.sky.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sky.constant.MessageConstant;
import com.sky.constant.RedisConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 员工信息 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2024-04-29
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //账户锁定时间到期，解锁
        if(employee.getStatus().equals(StatusConstant.DISABLE) && Boolean.FALSE.equals(redisTemplate.hasKey(RedisConstant.LOGIN_ERROR_COUNT))){
            employee.setStatus(StatusConstant.ENABLE);
            employeeMapper.updateById(employee);
        }

        //密码比对
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        if (!password.equals(employee.getPassword())) {
            //密码错误
            //记录错误次数，达到5次后，锁定该账户
            redisTemplate.opsForValue().increment(RedisConstant.LOGIN_ERROR_COUNT);
            Integer errorCount = (Integer) redisTemplate.opsForValue().get(RedisConstant.LOGIN_ERROR_COUNT);
            if (errorCount >= 5) {
                redisTemplate.expire(RedisConstant.LOGIN_ERROR_COUNT, Duration.ofHours(1).getSeconds(), TimeUnit.SECONDS);
                employee.setStatus(StatusConstant.DISABLE);
                employeeMapper.updateById(employee);
                throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
            }

            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus().equals(StatusConstant.DISABLE)) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //登录成功如果存在错误计数，去掉错误计数
       if(Boolean.TRUE.equals(redisTemplate.hasKey(RedisConstant.LOGIN_ERROR_COUNT))){
           redisTemplate.delete(RedisConstant.LOGIN_ERROR_COUNT);
       }
        //3、返回实体对象
        return employee;
    }

    @Override
    public IPage<Employee> selectPage(IPage<?> page, String name) {
       return this.employeeMapper.selectPage(page, name);
    }
}
