package com.lizheng.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lizheng.takeout.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author ZhengWen
 * @Date 2023/3/24 16:01
 * @Version 1.0
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
