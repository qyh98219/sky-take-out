package com.sky.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class EmployeeDTO implements Serializable {

    private Long id;

    @NotNull(message = "用户名不能为空")
    private String username;

    @NotNull(message = "姓名不能为空")
    private String name;

    @NotNull(message = "请输入正确的手机号")
    @Size(max = 11, min = 11, message = "请输入正确的手机号")
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "请输入正确的手机号")
    private String phone;

    @NotNull(message = "性别不能为空")
    private String sex;

    @NotNull(message = "身份证号码不正确")
    @Size(max = 18, min = 18, message = "身份证号码不正确")
    @Pattern(regexp = "^([1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)", message = "身份证号码不正确")
    private String idNumber;

}
