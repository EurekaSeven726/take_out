package com.lizheng.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lizheng.takeout.entity.Category;
import com.lizheng.takeout.mapper.CategoryMapper;
import com.lizheng.takeout.service.CategoryService;
import org.springframework.stereotype.Service;

/**
 * @Author ZhengWen
 * @Date 2023/3/18 10:01
 * @Version 1.0
 */
@Service
public class CategoryServicelmpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}