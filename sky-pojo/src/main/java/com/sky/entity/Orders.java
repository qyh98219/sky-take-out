package com.sky.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author baomidou
 * @since 2024-04-29
 */
@ApiModel(value = "Orders对象", description = "订单表")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("订单号")
    private String number;

    @ApiModelProperty("订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款")
    private Integer status;

    @ApiModelProperty("下单用户")
    private Long userId;

    @ApiModelProperty("地址id")
    private Long addressBookId;

    @ApiModelProperty("下单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderTime;

    @ApiModelProperty("结账时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkoutTime;

    @ApiModelProperty("支付方式 1微信,2支付宝")
    private Integer payMethod;

    @ApiModelProperty("支付状态 0未支付 1已支付 2退款")
    private Byte payStatus;

    @ApiModelProperty("实收金额")
    private BigDecimal amount;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("收货人")
    private String consignee;

    @ApiModelProperty("订单取消原因")
    private String cancelReason;

    @ApiModelProperty("订单拒绝原因")
    private String rejectionReason;

    @ApiModelProperty("订单取消时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cancelTime;

    @ApiModelProperty("预计送达时间")
    private LocalDateTime estimatedDeliveryTime;

    @ApiModelProperty("配送状态  1立即送出  0选择具体时间")
    private Boolean deliveryStatus;

    @ApiModelProperty("送达时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deliveryTime;

    @ApiModelProperty("打包费")
    private Integer packAmount;

    @ApiModelProperty("餐具数量")
    private Integer tablewareNumber;

    @ApiModelProperty("餐具数量状态  1按餐量提供  0选择具体数量")
    private Boolean tablewareStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAddressBookId() {
        return addressBookId;
    }

    public void setAddressBookId(Long addressBookId) {
        this.addressBookId = addressBookId;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public LocalDateTime getCheckoutTime() {
        return checkoutTime;
    }

    public void setCheckoutTime(LocalDateTime checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }

    public Byte getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Byte payStatus) {
        this.payStatus = payStatus;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public LocalDateTime getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(LocalDateTime cancelTime) {
        this.cancelTime = cancelTime;
    }

    public LocalDateTime getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    public void setEstimatedDeliveryTime(LocalDateTime estimatedDeliveryTime) {
        this.estimatedDeliveryTime = estimatedDeliveryTime;
    }

    public Boolean getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(Boolean deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Integer getPackAmount() {
        return packAmount;
    }

    public void setPackAmount(Integer packAmount) {
        this.packAmount = packAmount;
    }

    public Integer getTablewareNumber() {
        return tablewareNumber;
    }

    public void setTablewareNumber(Integer tablewareNumber) {
        this.tablewareNumber = tablewareNumber;
    }

    public Boolean getTablewareStatus() {
        return tablewareStatus;
    }

    public void setTablewareStatus(Boolean tablewareStatus) {
        this.tablewareStatus = tablewareStatus;
    }

    @Override
    public String toString() {
        return "Orders{" +
            "id = " + id +
            ", number = " + number +
            ", status = " + status +
            ", userId = " + userId +
            ", addressBookId = " + addressBookId +
            ", orderTime = " + orderTime +
            ", checkoutTime = " + checkoutTime +
            ", payMethod = " + payMethod +
            ", payStatus = " + payStatus +
            ", amount = " + amount +
            ", remark = " + remark +
            ", phone = " + phone +
            ", address = " + address +
            ", userName = " + userName +
            ", consignee = " + consignee +
            ", cancelReason = " + cancelReason +
            ", rejectionReason = " + rejectionReason +
            ", cancelTime = " + cancelTime +
            ", estimatedDeliveryTime = " + estimatedDeliveryTime +
            ", deliveryStatus = " + deliveryStatus +
            ", deliveryTime = " + deliveryTime +
            ", packAmount = " + packAmount +
            ", tablewareNumber = " + tablewareNumber +
            ", tablewareStatus = " + tablewareStatus +
        "}";
    }
}
