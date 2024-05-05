package com.sky.vo;

import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @ClassName OrderHistoryVO
 * @Description TODO
 * @Author qyh
 * @Date 2024/5/5 17:20
 * @Version 1.0
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderHistoryVO extends Orders {
    private List<OrderDetail> orderDetailList;
}
