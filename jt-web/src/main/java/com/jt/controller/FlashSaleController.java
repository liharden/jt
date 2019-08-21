package com.jt.controller;

import com.jt.annotation.DistriLimitAnno;
import com.jt.aspect.LimitAspect;
import com.jt.lock.DistributedLock;
import com.jt.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author liweihao
 * @Date 2019-08-12
 */
@Slf4j
@Controller
public class FlashSaleController {

    @Autowired
    OrderService orderService;
    @Autowired
    DistributedLock distributedLock;
    @Autowired
    LimitAspect limitAspect;
    //注意RedisTemplate用的String,String，后续所有用到的key和value都是String的
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    private static final String LOCK_PRE = "LOCK_ORDER";

    @PostMapping("/initCatalog")
    @ResponseBody
    public String initCatalog()  {
        try {

            orderService.initCatalog();
            log.info("进入initCatalog()方法！！！");
        } catch (Exception e) {
            log.error("error", e);
        }

        return "init is ok";
    }

    @PostMapping("/placeOrder")
    @ResponseBody
    @DistriLimitAnno(limitKey = "limit", limit = 100, seconds = "1")
    public Long placeOrder(Long orderId) {
        Long saleOrderId = 0L;
        boolean locked = false;
        String key = LOCK_PRE + orderId;
        String uuid = String.valueOf(orderId);
        try {
            locked = distributedLock.distributedLock(key, uuid, "10" );
            if(locked) {
                //直接操作数据库
//                saleOrderId = orderService.placeOrder(orderId);
                //操作缓存 异步操作数据库
                saleOrderId = orderService.placeOrderWithQueue(orderId);
            }
            log.info("saleOrderId:{}", saleOrderId);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if(locked) {
                distributedLock.distributedUnlock(key, uuid);
            }
        }
        return saleOrderId;
    }

}
