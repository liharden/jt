package com.jt.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author liweihao
 * @Date 2019-08-12
 */
@Data
public class SalesOrder {
    private Long id;
    private Long cid;
    private String name;
    private Date createTime;

}
