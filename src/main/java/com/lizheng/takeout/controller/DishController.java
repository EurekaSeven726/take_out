package com.lizheng.takeout.controller;

import com.lizheng.takeout.service.DishFlavorService;
import com.lizheng.takeout.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ZhengWen
 * @Date 2023/3/18 22:51
 * @Version 1.0
 */
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
}