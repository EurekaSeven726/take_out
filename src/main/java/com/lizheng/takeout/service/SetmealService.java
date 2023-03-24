package com.lizheng.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lizheng.takeout.dto.SetmealDto;
import com.lizheng.takeout.entity.Setmeal;

/**
 * @Author ZhengWen
 * @Date 2023/3/18 16:44
 * @Version 1.0
 */
public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);
    public SetmealDto getByIdWithDish(Long id);
    public void updateWithDish(SetmealDto setmealDto);
}