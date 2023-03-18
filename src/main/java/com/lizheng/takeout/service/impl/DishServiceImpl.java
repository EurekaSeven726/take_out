package com.lizheng.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lizheng.takeout.entity.Dish;
import com.lizheng.takeout.mapper.DishMapper;
import com.lizheng.takeout.service.DishService;
import org.springframework.stereotype.Service;

/**
 * @Author ZhengWen
 * @Date 2023/3/18 16:45
 * @Version 1.0
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}