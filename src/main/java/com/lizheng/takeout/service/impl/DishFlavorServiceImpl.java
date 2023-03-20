package com.lizheng.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lizheng.takeout.entity.DishFlavor;
import com.lizheng.takeout.mapper.DishFlavorMapper;
import com.lizheng.takeout.service.DishFlavorService;
import org.springframework.stereotype.Service;

/**
 * @Author ZhengWen
 * @Date 2023/3/18 22:51
 * @Version 1.0
 */
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>implements DishFlavorService {
}