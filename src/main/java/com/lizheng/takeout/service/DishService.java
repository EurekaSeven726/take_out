package com.lizheng.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lizheng.takeout.dto.DishDto;
import com.lizheng.takeout.entity.Dish;
import org.springframework.stereotype.Service;

/**
 * @Author ZhengWen
 * @Date 2023/3/18 16:42
 * @Version 1.0
 */
public interface DishService extends IService<Dish> {
    //新增菜品,同时插入菜品对应的口味数据,要同时操作dish和dish_flavor
    public void saveWithFlavor(DishDto dishDto);
    //根据id来查询菜品信息和对应的口味信息
    public DishDto getByIdWithFlavor(Long id);
    //更新菜品和口味
    public void updateWithFlavor(DishDto dishDto);
}
