package com.lizheng.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lizheng.takeout.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author ZhengWen
 * @Date 2023/3/25 11:08
 * @Version 1.0
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
