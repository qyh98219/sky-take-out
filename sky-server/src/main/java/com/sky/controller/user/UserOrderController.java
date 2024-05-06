package com.sky.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.constant.MessageConstant;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.*;
import com.sky.exception.OrderBusinessException;
import com.sky.result.Result;
import com.sky.service.*;
import com.sky.utils.OrderNumberUtil;
import com.sky.utils.ThreadLocalUtil;
import com.sky.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName UserOrderController
 * @Description TODO
 * @Author qyh
 * @Date 2024/5/5 12:52
 * @Version 1.0
 **/
@RequestMapping("/user/order")
@RestController
public class UserOrderController {
    @Autowired
    private IOrdersService ordersService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IOrderDetailService orderDetailService;
    @Autowired
    private IShoppingCartService shoppingCartService;
    @Autowired
    private IAddressBookService addressBookService;

    @PostMapping("/submit")
    public Result<OrderSubmitVO> submitOrder(@RequestBody OrdersSubmitDTO submitDTO){
        Long userId = (Long) ThreadLocalUtil.get("user_user_id");
        LocalDateTime orderTime = LocalDateTime.now();
        AddressBook addressBook = addressBookService.getById(submitDTO.getAddressBookId());
        Orders orders = new Orders();
        BeanUtils.copyProperties(submitDTO, orders);
        orders.setUserId(userId);
        orders.setUserName(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setOrderTime(orderTime);
        orders.setNumber(OrderNumberUtil.generateOrderNumber());
        String address = addressBook.getProvinceName() + addressBook.getCityName() + addressBook.getDistrictName()+addressBook.getDetail();
        orders.setAddress(address);
        if(!ordersService.save(orders)){
            throw new OrderBusinessException("下单失败");
        }

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(queryWrapper);
        List<OrderDetail> orderDetails = new ArrayList<>();
        OrderSubmitVO orderSubmitVO = new OrderSubmitVO();
        orderSubmitVO.setId(orders.getId());
        Integer orderNumber = 0;
        BigDecimal orderAmount = new BigDecimal("0");
        for (ShoppingCart shoppingCart: shoppingCarts){
            //保存订单详细
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orders.getId());
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            orderDetails.add(orderDetail);
            orderNumber += orderDetail.getNumber();
            orderAmount = orderAmount.add(orderDetail.getAmount());
        }
        orderSubmitVO.setOrderNumber(String.valueOf(orderNumber));
        orderSubmitVO.setOrderAmount(orderAmount);
        orderSubmitVO.setOrderTime(orderTime);
        orderDetailService.insertBatchSomeColumn(orderDetails);
        return Result.success(orderSubmitVO);
    }

    @GetMapping("/historyOrders")
    public Result<PageResult<OrderHistoryVO>> page(OrdersPageQueryDTO pageQueryDTO){
        Long userId = (Long) ThreadLocalUtil.get("user_user_id");
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, userId);
        if (Objects.nonNull(pageQueryDTO.getStatus())){
            queryWrapper.eq(Orders::getStatus, pageQueryDTO.getStatus());
        }
        queryWrapper.orderByDesc(Orders::getOrderTime);
        IPage<Orders> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        List<Orders> orders = ordersService.list(page, queryWrapper);
        List<OrderHistoryVO> orderHistoryVOS = new ArrayList<>(orders.size());
        for(Orders item : orders){
            OrderHistoryVO orderHistoryVO = new OrderHistoryVO();
            BeanUtils.copyProperties(item, orderHistoryVO);
            LambdaQueryWrapper<OrderDetail> qw = new LambdaQueryWrapper<>();
            qw.eq(OrderDetail::getOrderId, item.getId());
            List<OrderDetail> list = orderDetailService.list(qw);
            orderHistoryVO.setOrderDetailList(list);
            orderHistoryVOS.add(orderHistoryVO);
        }
        PageResult<OrderHistoryVO> pageResult = new PageResult<>();
        pageResult.setRecords(orderHistoryVOS);
        pageResult.setTotal(page.getTotal());
        return Result.success(pageResult);
    }

    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> getOrderDetial(@PathVariable Integer id){
        Orders orders = ordersService.getById(id);
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);

        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId, id);
        List<OrderDetail> orderDetails = orderDetailService.list(queryWrapper);
        orderVO.setOrderDetailList(orderDetails);
        return Result.success(orderVO);
    }
}
