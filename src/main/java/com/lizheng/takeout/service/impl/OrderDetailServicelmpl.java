package com.lizheng.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lizheng.takeout.entity.OrderDetail;
import com.lizheng.takeout.mapper.OrderDetailMapper;
import com.lizheng.takeout.service.OrderDetailService;
import com.lizheng.takeout.service.OrderService;
import org.springframework.stereotype.Service;

/**
 * @Author ZhengWen
 * @Date 2023/3/25 11:13
 * @Version 1.0
 */
@Service
public class OrderDetailServicelmpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
