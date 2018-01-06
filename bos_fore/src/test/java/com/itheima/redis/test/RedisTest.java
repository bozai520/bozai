package com.itheima.redis.test;

import org.junit.Test;

import redis.clients.jedis.Jedis;

public class RedisTest {
	//设置存储在redis中缓存的数据的生命周期
	@Test
	public void test01(){
		
		Jedis jedis = new Jedis("localhost");
		
		//设置键、生命周期的时间值、value值
		jedis.setex("bobo", 30, "黑马程序员");
		
		String value = jedis.get("bobo");
		
		System.out.println(value);
		
	}
}
