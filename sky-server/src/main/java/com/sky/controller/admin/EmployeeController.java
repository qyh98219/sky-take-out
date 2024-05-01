package com.sky.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.UserNameExistException;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.IEmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.utils.ThreadLocalUtil;
import com.sky.vo.EmployeeListVO;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    public ThreadLocal<Map<String,Object>> threadLocal = new ThreadLocal<>();

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>(1);
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    @PostMapping()
    @ApiOperation("员工添加")
    public Result addEmployee(@RequestBody @Validated EmployeeDTO employeeDTO){
        // 检查用户名是否重复
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employeeDTO.getUsername());

        Employee employee = employeeService.getOne(queryWrapper);
        if (!Objects.isNull(employee)) {
            throw new UserNameExistException("用户名已存在");
        }

        //保存员工信息
        employee = new Employee();
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));
        BeanUtils.copyProperties(employeeDTO, employee);
        //获取当前登录用户id
        Long createUser = ThreadLocalUtil.threadLocal.get();
        employee.setCreateUser(createUser);
        employee.setUpdateUser(createUser);

        if (!employeeService.save(employee)){
            return Result.error(MessageConstant.ACCOUNT_USER_SAVE_ERROR);
        }
        return Result.success("员工添加成功");
    }

    @GetMapping("/page")
    @ApiOperation("员工列表")
    public Result<EmployeeListVO> employeeList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(required = false) String name){
        Page<Employee> myPage = new Page<>(page, pageSize);
        IPage<Employee> resultPage = this.employeeService.selectPage(myPage, name);
        EmployeeListVO employeeListVO = new EmployeeListVO();
        employeeListVO.setTotal(resultPage.getTotal());
        employeeListVO.setRecords(resultPage.getRecords());
        return Result.success(employeeListVO);
    }

    /**
     * @Author qyh
     * @Description 禁用/启用员工账号
     * @Date 9:19 2024/5/1
     * @Param [status, empId]
     * @return com.sky.result.Result
     **/
    @PostMapping("/status/{status}")
    @ApiOperation("启用、禁用员工")
    public Result updateEmployeeStatus(@PathVariable("status") Integer status, Integer id){
        Employee employee = employeeService.getById(id);

        if(Objects.isNull(employee)){
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        employee.setStatus(status);
        if(!employeeService.updateById(employee)){
            return Result.error("操作失败");
        }
        return Result.error("操作成功");
    }

    @GetMapping("/{id}")
    @ApiOperation("员工信息回显")
    public Result<Employee> reviewEmployee(@PathVariable("id") Integer id){
        Employee employee = employeeService.getById(id);

        return Result.success(employee);
    }

    @PutMapping()
    @ApiOperation("员工信息编辑")
    public Result updateEmployee(@RequestBody @Validated Employee employee){
        //判断是否有修改账号名，如果有要判断是否有重复
        Employee oldEmployee = employeeService.getById(employee.getId());
        if(Objects.isNull(oldEmployee)){
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //有修改账号名
        if(!oldEmployee.getUsername().equals(employee.getUsername())){
            LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Employee::getName, employee.getName());

            Employee one = employeeService.getOne(queryWrapper);
            if (!Objects.isNull(one)){
                throw new UserNameExistException(MessageConstant.ACCOUNT_USER_NAME_EXIST);
            }
        }



        if(!employeeService.updateById(employee)){
            return Result.error("操作失败");
        }
        return Result.success();
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("员工登出")
    public Result<String> logout() {
        threadLocal.remove();
        return Result.success();
    }

}
