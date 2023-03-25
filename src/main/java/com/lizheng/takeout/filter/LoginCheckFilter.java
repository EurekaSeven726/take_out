package com.lizheng.takeout.filter;

import com.alibaba.fastjson.JSON;
import com.lizheng.takeout.common.BaseContext;
import com.lizheng.takeout.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否登陆
 * @Author ZhengWen
 * @Date 2023/3/12 22:02
 * @Version 1.0
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
//所有的页面都要检查捏
@Slf4j
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
//路径匹配器
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        //向下转型
        HttpServletResponse response=(HttpServletResponse) servletResponse;
        //1、获取本次请求的URI
        String requestURI = request.getRequestURI();// /backend/index.html
        log.info("拦截到请求：{}",requestURI);
        //定义不需要处理的请求路径
        long id = Thread.currentThread().getId() ;
        log.info("线程id:{}" ,id);
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };
//        String[] urls = new String[]{
//                "/employee/login",
//                "/employee/logout",
//                "/backend/**",
//                "/front/**",
//                "/common/**",
//                "/user/sendMsg",
//                // 发送登录验证码
//                "/user/login",
//                // 用户登录
//        };

        //2、判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

        //3、如果不需要处理，则直接放行
        if(check){
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }

        //4、判断登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("employee") != null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("employee"));
            Long empId= (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request,response);
            return;
        }
        //4.2 判断登陆状态，如果已登陆，则直接放行
        if (request.getSession().getAttribute("user")!=null){
            log.info("监测到已登陆,id为:{}",request.getSession().getAttribute("user"));

            //调用我们基于ThreadLocal的工具类，将用户id存储到线程中，方便在自动填充时取出用户id
            Long employeeId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(employeeId);

            filterChain.doFilter(request,response); //放行
            return; //若放行 后面代码无需执行 直接return
        }
//        log.info("拦截到请求:{}",request.getRequestURI());
//        //{}在这里算是一个占位符,后面对应的内容会到这里面
//        filterChain.doFilter(request,response);
//        //请求和回应都传进去
        log.info("用户未登录");
        //5、如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
        // 6.如果需要处理，判断C端用户是否登录
//        if (request.getSession().getAttribute("user") != null) {
//            // 能进入说明已经登录，直接放行
//            Long userId = (Long) request.getSession().getAttribute("user");
//            log.info("手机用户{}已登录", userId);
//            // 把当前登录用户的ID保存到ThreadLocal中
//            BaseContext.setCurrentUserId(userId);
//            // 放行
//            filterChain.doFilter(request, response);
//            return;
//        }

    }
    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            //遍历url
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }


}
