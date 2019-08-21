package com.jt.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.Catalog;
import org.apache.ibatis.annotations.*;

/**
 * @author liweihao
 * @Date 2019-08-12
 */

public interface CatalogMapper extends BaseMapper<Catalog>{

    @Insert("insert into catalog (name, total, sold) values (#{name}, #{total}, #{sold}) ")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    Long insertCatalog(Catalog catalog);

    @Update("update catalog set name=#{name}, total=#{total}, sold=#{sold} where id=#{id}")
    Long updateCatalog(Catalog catalog);

    @Select("select * from catalog where id=#{id}")
    Catalog selectCatalog(@Param("id") Long id);

}
