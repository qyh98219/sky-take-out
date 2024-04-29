package com.sky.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 地址簿
 * </p>
 *
 * @author baomidou
 * @since 2024-04-29
 */
@TableName("address_book")
@ApiModel(value = "AddressBook对象", description = "地址簿")
public class AddressBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("收货人")
    private String consignee;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("省级区划编号")
    private String provinceCode;

    @ApiModelProperty("省级名称")
    private String provinceName;

    @ApiModelProperty("市级区划编号")
    private String cityCode;

    @ApiModelProperty("市级名称")
    private String cityName;

    @ApiModelProperty("区级区划编号")
    private String districtCode;

    @ApiModelProperty("区级名称")
    private String districtName;

    @ApiModelProperty("详细地址")
    private String detail;

    @ApiModelProperty("标签")
    private String label;

    @ApiModelProperty("默认 0 否 1是")
    private Boolean isDefault;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public String toString() {
        return "AddressBook{" +
            "id = " + id +
            ", userId = " + userId +
            ", consignee = " + consignee +
            ", sex = " + sex +
            ", phone = " + phone +
            ", provinceCode = " + provinceCode +
            ", provinceName = " + provinceName +
            ", cityCode = " + cityCode +
            ", cityName = " + cityName +
            ", districtCode = " + districtCode +
            ", districtName = " + districtName +
            ", detail = " + detail +
            ", label = " + label +
            ", isDefault = " + isDefault +
        "}";
    }
}
