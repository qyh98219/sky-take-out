package com.sky.dto;

import com.sky.validiton.EnumValue;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class CategoryDTO implements Serializable {

    //主键
    private Long id;

    //类型 1 菜品分类 2 套餐分类
    @NotNull(message = "分类不能为空")
    @EnumValue(intValues = {1,2})
    private Integer type;

    //分类名称
    @NotNull
    @Length(min = 2, max= 20, message = "分类名称输入不符")
    private String name;

    //排序
    @NotNull
    @Pattern(regexp = "^[0-99]*$", message = "排序必须为整数")
    private Integer sort;

}
