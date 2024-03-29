package com.jt.pojo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.experimental.Accessors;

@JsonIgnoreProperties(ignoreUnknown=true) 
//表示JSON转化时忽略未知属性
@TableName("tb_item")
@Data
@Accessors(chain=true)
public class Item extends BasePojo{
	@TableId(type=IdType.AUTO)
	private Long id;				//商品id
	private String title;			//商品标题
	private String sellPoint;		//商品卖点信息
	private Long   price;			//商品价格 Long > dubbo  价格* 100=198
									//duubo精度问题 0.1111111...+0.888888889=0.9999999.....
	private Integer num;			//商品数量
	private String barcode;			//条形码
	private String image;			//商品图片信息   1.jpg,2.jpg,3.jpg
	private Long   cid;				//表示商品的分类id
	private Integer status;			//1正常，2下架
	
	/**
	 * <input type="hidden" class="text" name="itemImage" 
	 * value="${item.images[0]}"/>
	 * @return
	 */
	//为了满足页面调用需求,添加get方法
	public String[] getImages(){
		
		return image.split(",");
	}
}
