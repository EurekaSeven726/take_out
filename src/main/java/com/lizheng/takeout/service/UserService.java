package com.lizheng.takeout.service;

/**
 * @Author ZhengWen
 * @Date 2023/3/23 22:54
 * @Version 1.0
 */
import com.baomidou.mybatisplus.extension.service.IService;
import com.lizheng.takeout.entity.User;

public interface UserService extends IService<User> {
    //发送邮件
    void sendMsg(String to,String subject,String text);
}