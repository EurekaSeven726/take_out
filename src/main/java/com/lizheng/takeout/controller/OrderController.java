package com.lizheng.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lizheng.takeout.common.BaseContext;
import com.lizheng.takeout.common.R;
import com.lizheng.takeout.dto.OrdersDto;
import com.lizheng.takeout.entity.*;
import com.lizheng.takeout.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author ZhengWen
 * @Date 2023/3/25 11:20
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }
    //订单管理
    @Transactional
    @GetMapping("/userPage")
    public R<Page> userPage(int page, int pageSize){
        //构造分页构造器
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>();
//        //构造条件构造器
//        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Orders::getUserId, BaseContext.getThreadLocalId());
//        //添加排序条件
//        queryWrapper.orderByDesc(Orders::getOrderTime);
//        //进行分页查询
//        orderService.page(pageInfo,queryWrapper);
//        //对象拷贝
//        BeanUtils.copyProperties(pageInfo,ordersDtoPage,"records");
//        List<Orders> records=pageInfo.getRecords();
//        List<OrdersDto> list = records.stream().map((item) -> {
//            OrdersDto ordersDto = new OrdersDto();
//            BeanUtils.copyProperties(item, ordersDto);
//            Long Id = item.getId();
//            //根据id查分类对象
//            Orders orders = orderService.getById(Id);
//            String number = orders.getNumber();
//            LambdaQueryWrapper<OrderDetail> lambdaQueryWrapper=new LambdaQueryWrapper<>();
//            lambdaQueryWrapper.eq(OrderDetail::getOrderId,number);
//            List<OrderDetail> orderDetailList = orderDetailService.list(lambdaQueryWrapper);
//            int num=0;
//            for(OrderDetail l:orderDetailList){
//                num+=l.getNumber().intValue();
//            }
//            ordersDto.setSumNum(num);
//            return ordersDto;
//        }).collect(Collectors.toList());
//        ordersDtoPage.setRecords(list);
//        return R.success(ordersDtoPage);
        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getThreadLocalId());
        queryWrapper.orderByAsc(Orders::getOrderTime);

        orderService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, ordersDtoPage, "records");


        List<Orders> records =pageInfo.getRecords();
        List<OrdersDto> list=records.stream().map((item)->{

            OrdersDto ordersDto=new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);

            Long orderId=item.getId();
            LambdaQueryWrapper<OrderDetail> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(OrderDetail::getOrderId, orderId);
            List<OrderDetail> res = orderDetailService.list(queryWrapper1);
            ordersDto.setOrderDetails(res);
            return ordersDto;
        }).collect(Collectors.toList());


        ordersDtoPage.setRecords(list);

        log.info("查询订单数据为：{}", ordersDtoPage);
        return R.success(ordersDtoPage);
    }

    //再来一单
    @Transactional
    @PostMapping("/again")
    public R<String> again(@RequestBody Orders orders){
        log.info(orders.toString());
        //设置用户id 指定当前是哪个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        //得到订单id
        Long ordersId = orders.getId();
        LambdaQueryWrapper<Orders> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getId,ordersId);
        //根据订单id得到订单元素
        Orders one = orderService.getOne(queryWrapper);
        //得到订单表中的number 也就是订单明细表中的order_id
        String number = one.getNumber();

        LambdaQueryWrapper<OrderDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(OrderDetail::getOrderId,number);
        //根据订单明细表的order_id得到订单明细集合
        List<OrderDetail> orderDetails = orderDetailService.list(lambdaQueryWrapper);
        //新建购物车集合
        List<ShoppingCart> shoppingCarts = new ArrayList<>();
        //通过用户id把原来的购物车给清空
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);
        //遍历订单明细集合,将集合中的元素加入购物车集合
        for (OrderDetail orderDetail : orderDetails) {
            ShoppingCart shoppingCart = new ShoppingCart();
            //得到菜品id或套餐id
            Long dishId = orderDetail.getDishId();
            Long setmealId = orderDetail.getSetmealId();
            //添加购物车部分属性
            shoppingCart.setUserId(currentId);
            shoppingCart.setDishFlavor(orderDetail.getDishFlavor());
            shoppingCart.setNumber(orderDetail.getNumber());
            shoppingCart.setAmount(orderDetail.getAmount());
            shoppingCart.setCreateTime(LocalDateTime.now());
            if(dishId!=null){
                //订单明细元素中是菜品
                LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
                dishLambdaQueryWrapper.eq(Dish::getId,dishId);
                //根据订单明细集合中的单个元素获得单个菜品元素
                Dish dishone = dishService.getOne(dishLambdaQueryWrapper);
                //根据菜品信息添加购物车信息
                shoppingCart.setDishId(dishId);
                shoppingCart.setName(dishone.getName());
                shoppingCart.setImage(dishone.getImage());
                //调用保存购物车方法
                shoppingCarts.add(shoppingCart);
            }
            else if(setmealId!=null){
                //订单明细元素中是套餐
                LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
                setmealLambdaQueryWrapper.eq(Setmeal::getId,setmealId);
                //根据订单明细集合中的单个元素获得单个套餐元素
                Setmeal setmealone = setmealService.getOne(setmealLambdaQueryWrapper);
                //根据套餐信息添加购物车信息
                shoppingCart.setSetmealId(setmealId);
                shoppingCart.setName(setmealone.getName());
                shoppingCart.setImage(setmealone.getImage());
                //调用保存购物车方法
                shoppingCarts.add(shoppingCart);
            }
        }
        shoppingCartService.saveBatch(shoppingCarts);
        return R.success("操作成功");
    }
    //第一种
//    public R<String> again(@RequestBody Orders order1){
//        //取得orderId
//        Long id = order1.getId();
//        Orders orders = orderService.getById(id);
//        //获取当前用户id
//        Long userid = BaseContext.getCurrentId();
//        //取得订单详细信息
//
//        LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(OrderDetail::getOrderId, id);
//
//        List<OrderDetail> OrderDetails = orderDetailService.list(wrapper);
//        long shoppingCartId = IdWorker.getId();//购物车id
//        List<ShoppingCart> shoppingcarts = OrderDetails.stream().map((item) -> {
//            ShoppingCart shoppingCart = new ShoppingCart();
//            shoppingCart.setId(shoppingCartId);
//            shoppingCart.setName(item.getName());
//            shoppingCart.setImage(item.getImage());
//            shoppingCart.setUserId(userid);
//            shoppingCart.setNumber(item.getNumber());
//            shoppingCart.setDishFlavor(item.getDishFlavor());
//            shoppingCart.setDishId(item.getDishId());
//            shoppingCart.setSetmealId(item.getSetmealId());
//            shoppingCart.setAmount(item.getAmount());
//            shoppingCart.setCreateTime(LocalDateTime.now());
//            return shoppingCart;
//        }).collect(Collectors.toList());
//        shoppingCartService.saveBatch(shoppingcarts);
//
//        return R.success("再来一单");
//    }

    //第二种
//        //设置订单号码
//        long orderId = IdWorker.getId();
//        orders.setId(orderId);
//        //设置订单号码
//        String number = String.valueOf(IdWorker.getId());
//        orders.setNumber(number);
//        //设置下单时间
//        orders.setOrderTime(LocalDateTime.now());
//        orders.setCheckoutTime(LocalDateTime.now());
//        orders.setStatus(2);
//        //向订单表中插入一条数据
//        orderService.save(orders);
//        //修改订单明细表
//        LambdaQueryWrapper<OrderDetail> queryWrapper=new LambdaQueryWrapper<>();
//        queryWrapper.eq(OrderDetail::getOrderId,id);
//        List<OrderDetail> list = orderDetailService.list(queryWrapper);
//        list.stream().map((item)->{
//            //订单明细表id
//            long detailId = IdWorker.getId();
//            //设置订单号码
//            //item.setOrderId(orderId);
//            item.setId(detailId);
//            return item;
//        }).collect(Collectors.toList());
//
//        //向订单明细表中插入多条数据
//        orderDetailService.saveBatch(list);
//        return R.success("再来一单");

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String number,String beginTime,String endTime){
        //构造分页构造器
        Page<Orders> pageInfo = new Page<>(page, pageSize);

        Page<OrdersDto> ordersDtoPage=new Page<>();
        //构造条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        //根据number进行模糊查询
        queryWrapper.like(!StringUtils.isEmpty(number),Orders::getNumber,number);
        //根据Datetime进行时间范围查询

//        log.info("开始时间：{}",beginTime);
//        log.info("结束时间：{}",endTime);
        if(beginTime!=null&&endTime!=null){
            queryWrapper.ge(Orders::getOrderTime,beginTime);
            queryWrapper.le(Orders::getOrderTime,endTime);
        }
        //添加排序条件
        queryWrapper.orderByDesc(Orders::getOrderTime);

        //进行分页查询
        orderService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,ordersDtoPage,"records");

        List<Orders> records=pageInfo.getRecords();

        List<OrdersDto> list=records.stream().map((item)->{
            OrdersDto ordersDto=new OrdersDto();

            BeanUtils.copyProperties(item,ordersDto);
            String name="用户"+item.getUserId();
            ordersDto.setUserName(name);
            return ordersDto;
        }).collect(Collectors.toList());

        ordersDtoPage.setRecords(list);
        return R.success(ordersDtoPage);
    }
    @PutMapping
    public R<String> send(@RequestBody Orders orders){
        Long id = orders.getId();
        Integer status = orders.getStatus();
        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getId,id);
        Orders one = orderService.getOne(queryWrapper);
        one.setStatus(status);
        orderService.updateById(one);
        return R.success("派送成功");

    }


}