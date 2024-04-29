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
 * 订单明细表
 * </p>
 *
 * @author baomidou
 * @since 2024-04-29
 */
@TableName("order_detail")
@ApiModel(value = "OrderDetail对象", description = "订单明细表")
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("名字")
    private String name;

    @ApiModelProperty("图片")
    private String image;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("菜品id")
    private Long dishId;

    @ApiModelProperty("套餐id")
    private Long setmealId;

    @ApiModelProperty("口味")
    private String dishFlavor;

    @ApiModelProperty("数量")
    private Integer number;

    @ApiModelProperty("金额")
    private BigDecimal amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public Long getSetmealId() {
        return setmealId;
    }

    public void setSetmealId(Long setmealId) {
        this.setmealId = setmealId;
    }

    public String getDishFlavor() {
        return dishFlavor;
    }

    public void setDishFlavor(String dishFlavor) {
        this.dishFlavor = dishFlavor;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
            "id = " + id +
            ", name = " + name +
            ", image = " + image +
            ", orderId = " + orderId +
            ", dishId = " + dishId +
            ", setmealId = " + setmealId +
            ", dishFlavor = " + dishFlavor +
            ", number = " + number +
            ", amount = " + amount +
        "}";
    }
}
