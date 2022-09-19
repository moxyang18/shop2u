package com.moxuan.shop2u.cache;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 *
 */
public class JedisPoolWrapper {
	private JedisPool jedisPool;

	public JedisPoolWrapper(final JedisPoolConfig poolConfig, final String host,
			final int port) {
		try {
			jedisPool = new JedisPool(poolConfig, host, port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

}
