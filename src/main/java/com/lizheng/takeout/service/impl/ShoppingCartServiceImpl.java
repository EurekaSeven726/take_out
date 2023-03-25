package com.lizheng.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lizheng.takeout.entity.ShoppingCart;
import com.lizheng.takeout.mapper.ShoppingCartMapper;
import com.lizheng.takeout.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @Author ZhengWen
 * @Date 2023/3/25 10:55
 * @Version 1.0
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl <ShoppingCartMapper,ShoppingCart> implements ShoppingCartService {

}
