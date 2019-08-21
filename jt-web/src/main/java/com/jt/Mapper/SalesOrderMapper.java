package com.jt.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.SalesOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author liweihao
 * @Date 2019-08-12
 */
public interface SalesOrderMapper extends BaseMapper<SalesOrder>{

    @Insert("insert sales_order (cid, name) values (#{cid}, #{name})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void insertSalesOrder(SalesOrder salesOrder);

    @Update("update sales_order set cid=#{cid}, name=#{name} where id=#{id}")
    Long updateSalesOrder(SalesOrder salesOrder);

    @Select("select * from sales_order where id=#{id}")
    SalesOrder selectSalesOrder(@Param("id") Long id);

    @Delete("Delete from sales_order where id=#{id}")
    Long deleteSalesOrder(@Param("id") Long id);

    @Select("select * from sales_order")
    List<SalesOrder> selectAllSalesOrder();
}
