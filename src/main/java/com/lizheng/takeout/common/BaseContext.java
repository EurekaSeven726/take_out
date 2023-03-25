package com.lizheng.takeout.common;

/**
 * @Author ZhengWen
 * @Date 2023/3/17 22:48
 * @Version 1.0
 */
/**
 * 基于ThreadLocal封装的工具类，用于保存和获取当前登录用户的id
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();
//因为是存当前用户的id,所以是long型
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
