package com.tuling.controller;

import com.netflix.discovery.converters.Auto;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tuling.api.MsCustomFeignOrderApi;
import com.tuling.api.OrderApi;
import com.tuling.entity.OrderVo;
import com.tuling.entity.User;
import com.tuling.entity.UserInfoVo;
import com.tuling.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by smlz on 2019/3/26.
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private OrderApi orderApi;

    @Autowired
    private MsCustomFeignOrderApi msCustomFeignOrderApi;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/queryUserInfoById/{userId}")
    public UserInfoVo queryUserInfoById(@PathVariable("userId") Integer userId) {
        User user = userServiceImpl.queryUserById(userId);

        List<OrderVo> voList = orderApi.queryOrdersByUserId(userId);

        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setOrderVoList(voList);
        userInfoVo.setUserName(user.getUserName());
        return userInfoVo;
    }

    @RequestMapping("/queryUserInfoById4Ribbon/{userId}")
    @HystrixCommand(fallbackMethod = "queryUserInfoById4RibbonFallBack")
    public UserInfoVo queryUserInfoById4Ribbon(@PathVariable("userId") Integer userId) {
        User user = new User();
        user.setUserName("张三");

        ResponseEntity<List> responseEntity = restTemplate.getForEntity("http://MS-PROVIDER-ORDER/order/queryOrdersByUserId/"+userId,List.class);

        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setOrderVoList(responseEntity.getBody());
        userInfoVo.setUserName(user.getUserName());
        return userInfoVo;
    }

    public UserInfoVo queryUserInfoById4RibbonFallBack(Integer userId) {
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setUserName("-1");
        userInfoVo.setOrderVoList(null);
        return userInfoVo;
    }


    @GetMapping("/getIpAndPort")
    public String getIpAndPort() {
        return orderApi.info();
    }

    @RequestMapping("/queryUserInfoByIdByCustomFeign/{userId}")
    public UserInfoVo queryUserInfoByIdByCustomFeign(@PathVariable("userId") Integer userId) {
        User user = new User();
        user.setUserName("张三");

        List<OrderVo> voList = msCustomFeignOrderApi.queryOrdersByUserId(userId);

        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setOrderVoList(voList);
        userInfoVo.setUserName(user.getUserName());
        return userInfoVo;
    }


}
