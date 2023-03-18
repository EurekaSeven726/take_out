package com.lizheng.takeout.controller;

/**
 * @Author ZhengWen
 * @Date 2023/3/11 23:36
 * @Version 1.0
 */

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lizheng.takeout.common.R;
import com.lizheng.takeout.entity.Employee;
import com.lizheng.takeout.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登陆功能
     * @param request  保存
     * @param employee  返回内容保存到employee里
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        //Digest Utils用来做md5加密处理
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
/*
        通过.eq进行等值查询   Lambda插件里Employee::就是Employee调用getUserName方法，前面员工是调用数据库里的值，后面带括号是是浏览器里得到的值
        把得到的值即employee.getUsername()用Employee::getUsername查询数据库里有没有
*/
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        /*
        用getone方法，调出出唯一的那个对应数据，查出来后封装成一个Employee对象emp

        * */
        Employee emp = employeeService.getOne(queryWrapper);

        //如果没有查询到则返回登录失败结果
        if(emp == null){
            return R.error("登陆失败，未能查询到此用户");
        }

        //密码比对，如果不一致则返回登录失败结果
        if(!emp.getPassword().equals(password)){
            return R.error("登录失败,你输入的密码不对捏");
        }

        //查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if(emp.getStatus() == 0){
            return R.error("登录失败,账号已禁用");
        }

        //登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);


    }

    /**
     * 员工退出功能
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理Session中当前登陆员工的id
        request.getSession().removeAttribute("employee");
        return R.success("当前用户已退出");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request ,@RequestBody Employee employee){
        log.info("添加了员工,其信息为{}",employee.toString());
        //设置初始密码123456,进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        //get当前登陆用户id
//        Long empId= (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
        employeeService.save(employee);
        return R.success("添加新员工成功");
    }
//    @DeleteMapping("/{id}")
//    public R<String> delete(HttpServletRequest request ,@RequestBody Employee employee){
//        log.info("删除员工,其信息为{}",employee.toString());
//        employeeService.removeById(employee);
//        return R.success("删除员工成功");
//    }
    @DeleteMapping
    public R<String> delete(Long id){
        log.info("删除分类，id为{}",id);
        employeeService.removeById(id);
        //代码完善之后categoryService.remove(ids);
        return R.success("分类信息删除成功");
    }
    /**
     * 员工信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page = {},pageSize = {},name = {}" ,page,pageSize,name);

        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }
    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
        long id=Thread.currentThread().getId();
        log.info("线程id为+{}",id);

//        Long empId = (Long)request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
        employeeService.updateById(employee);

        return R.success("员工信息修改成功");
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息...");
        Employee employee = employeeService.getById(id);
        if(employee != null){
            return R.success(employee);
        }
        return R.error("没有查询到对应员工信息");
    }
}
