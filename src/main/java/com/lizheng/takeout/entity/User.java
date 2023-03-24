package com.lizheng.takeout.entity;

/**
 * @Author ZhengWen
 * @Date 2023/3/24 10:28
 * @Version 1.0
 */

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
/**
 * 用户信息
 */
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    //姓名
    private String name;
    //手机号
    private String phone;
    //性别 0 女 1 男
    private String sex;
    //身份证号
    private String idNumber;
    //头像
    private String avatar;
    //状态 0:禁用，1:正常
    private Integer status;
}
