package com.jt.pojo;

import lombok.Data;

/**
 * @author liweihao
 * @Date 2019-08-12
 */
@Data
public class Catalog {
    private Long id;
    private String name;
    private Long total;
    private Long sold;
    private Long version;
}
