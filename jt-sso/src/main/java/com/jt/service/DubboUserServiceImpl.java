package com.jt.service;

import java.util.Date;
import java.util.UUID;

import com.jt.util.AESUtil;
import com.jt.util.MD5Util;
import com.jt.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;

@Service(timeout = 3000) //将对象交给dubbo管理
public class DubboUserServiceImpl implements DubboUserService {
	
	@Autowired
	private JedisCluster jedisCluster;
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public void insertUser(User user) {
		//密码加密  注意加密和登录算法必须相同
		String md5Pass = MD5Util.encrypt(user.getPassword());
		String email = AESUtil.encrypt(user.getEmail(), "email");
		String phone = AESUtil.encrypt(user.getPhone(), "phone");
		user.setPassword(md5Pass).setEmail(email).setPhone(phone).setCreated(new Date()).setUpdated(user.getCreated());
		userMapper.insert(user);
	}

	@Override
	public String doLogin(User user) {
		String md5Pass = MD5Util.encrypt(user.getPassword());
		user.setPassword(md5Pass);
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>(user);
		User userDB = userMapper.selectOne(queryWrapper);
		String key = null;
		if(userDB!=null) {
			//表示用户名密码正确    UUID
			key = MD5Util.encrypt(UUIDUtil.getUuid());
			//数据脱敏处理
			userDB.setPassword("123456");
			String userJSON = ObjectMapperUtil.toJSON(userDB);
			jedisCluster.setex(key,7*24*3600, userJSON);
		}
		return key;
	}
}
