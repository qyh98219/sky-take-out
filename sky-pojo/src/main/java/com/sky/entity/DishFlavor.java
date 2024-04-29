package com.sky.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 菜品口味关系表
 * </p>
 *
 * @author baomidou
 * @since 2024-04-29
 */
@TableName("dish_flavor")
@ApiModel(value = "DishFlavor对象", description = "菜品口味关系表")
public class DishFlavor implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("菜品")
    private Long dishId;

    @ApiModelProperty("口味名称")
    private String name;

    @ApiModelProperty("口味数据list")
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DishFlavor{" +
            "id = " + id +
            ", dishId = " + dishId +
            ", name = " + name +
            ", value = " + value +
        "}";
    }
}
