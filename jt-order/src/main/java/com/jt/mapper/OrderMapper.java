package com.jt.mapper;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.Order;

public interface OrderMapper extends BaseMapper<Order>{
	
	Order findOrderById(String id);
	
}
