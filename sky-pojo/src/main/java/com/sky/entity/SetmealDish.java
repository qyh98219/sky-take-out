package com.sky.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 套餐菜品关系
 * </p>
 *
 * @author baomidou
 * @since 2024-04-29
 */
@TableName("setmeal_dish")
@ApiModel(value = "SetmealDish对象", description = "套餐菜品关系")
public class SetmealDish implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("套餐id")
    private Long setmealId;

    @ApiModelProperty("菜品id")
    private Long dishId;

    @ApiModelProperty("菜品名称 （冗余字段）")
    private String name;

    @ApiModelProperty("菜品单价（冗余字段）")
    private BigDecimal price;

    @ApiModelProperty("菜品份数")
    private Integer copies;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSetmealId() {
        return setmealId;
    }

    public void setSetmealId(Long setmealId) {
        this.setmealId = setmealId;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCopies() {
        return copies;
    }

    public void setCopies(Integer copies) {
        this.copies = copies;
    }

    @Override
    public String toString() {
        return "SetmealDish{" +
            "id = " + id +
            ", setmealId = " + setmealId +
            ", dishId = " + dishId +
            ", name = " + name +
            ", price = " + price +
            ", copies = " + copies +
        "}";
    }
}
