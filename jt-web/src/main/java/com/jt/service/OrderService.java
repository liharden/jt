package com.jt.service;

/**
 * @author liweihao
 * @Date 2019-08-12
 */
public interface OrderService {

    void initCatalog();

    Long placeOrder(Long catalogId);

    Long placeOrderWithQueue(Long catalogId);

}
