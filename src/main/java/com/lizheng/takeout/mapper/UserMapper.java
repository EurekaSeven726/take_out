package com.lizheng.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lizheng.takeout.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author ZhengWen
 * @Date 2023/3/24 10:31
 * @Version 1.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}