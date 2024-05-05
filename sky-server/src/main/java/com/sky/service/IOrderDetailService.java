package com.sky.service;

import com.sky.entity.OrderDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * <p>
 * 订单明细表 服务类
 * </p>
 *
 * @author baomidou
 * @since 2024-04-29
 */
public interface IOrderDetailService extends IService<OrderDetail> {
    int insertBatchSomeColumn(List<OrderDetail> entityList);
}
