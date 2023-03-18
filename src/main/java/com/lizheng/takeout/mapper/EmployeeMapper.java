package com.lizheng.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lizheng.takeout.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author ZhengWen
 * @Date 2023/3/11 23:30
 * @Version 1.0
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
