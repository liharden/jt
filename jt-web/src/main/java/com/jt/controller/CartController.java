package com.jt.controller;


import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.service.DubboCartService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Controller
@RequestMapping("/cart")
public class CartController {

	//加检查check = false会在服务启动时不检查是否注入，在调用在检查
	@Reference(timeout = 3000,check = false)
	private DubboCartService dubboCartService;
	/**
	 * 	实现购物车列表展示
	 */
	
	@HystrixCommand(fallbackMethod = "createFall")
//	@RequestMapping("/show")
	@RequestMapping("/add/{itemId}")
	public String cartList(Cart cart,Model model) {
		Long userId = UserThreadLocal.get().getId();
		cart.setUserId(userId);
		System.out.println(cart);
		dubboCartService.insertCart(cart);
		List<Cart> list = dubboCartService.findCartListByUserId(userId);
		model.addAttribute("cartList", list);
		return "/cart";
	}
	@HystrixCommand(fallbackMethod = "showFall")
	@RequestMapping("/show")
	public String show(Model model) {
		Long userId = UserThreadLocal.get().getId();
		List<Cart> list = dubboCartService.findCartListByUserId(userId);
		model.addAttribute("cartList", list);
		return "/cart";
	}
	public String showFall(Model model) {
//		System.out.println("12312312312111111111111111111111111");
		model.addAttribute("fallMsg", "1");
		return "/index";
	}
	public String createFall(Cart cart,Model model) {
//		System.out.println("12312312312111111111111111111111111");
		return "redirect:/items/562379.html?fallMsg=1";
	}
	/**
	 *	 修改购物车商品信息和数量
	 * 	 规则：如果{参数}的名称与对象中的属性一致，则可以使用对象直接取值
	 */
	@RequestMapping("/update/num/{itemId}/{num}")
	@ResponseBody
	public SysResult updateNum(Cart cart) {
		cart.setUserId(UserThreadLocal.get().getId());
		dubboCartService.updateNum(cart);
		return SysResult.success();
	}
	
	@RequestMapping("/delete/{itemId}")
	public String deleteCart(Cart cart) {
		Long userId = UserThreadLocal.get().getId();
		cart.setUserId(userId);
		dubboCartService.deleteCart(cart);
		return "redirect:/item.html";
	}
}
