package com.itheima.redis.test;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;
/**
 * 使用springData整合redis
 * @author Administrator
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class SpringDataRedisTest {
	
	//注入一个SpringDataJedis的模板对象
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Test
	public void test01(){
		redisTemplate.opsForValue().set("猪头", "我爱你", 30, TimeUnit.SECONDS);
		String value = redisTemplate.opsForValue().get("猪头");
		System.out.println(value);
	}
	
}
