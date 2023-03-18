package com.lizheng.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lizheng.takeout.entity.Employee;
import com.lizheng.takeout.mapper.EmployeeMapper;
import com.lizheng.takeout.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @Author ZhengWen
 * @Date 2023/3/11 23:33
 * @Version 1.0
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>implements EmployeeService {
}
