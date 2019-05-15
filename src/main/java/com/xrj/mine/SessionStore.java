package com.xrj.mine;

import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;

public class SessionStore {
	// private Hessian2Serialization h2Serialization = new Hessian2Serialization();
	// // private RedisUtil redisUtil;
	private static Jedis jedis = new Jedis("127.0.0.1", 6379);

	public static Map<String, Object> getSession(String sid) {
		// Map<String, Object> session = getService().getSession(sid);
		// return session == null ? new HashMap<>() : JSON.parseObject((String)
		// session.get("json"), HashMap.class);

		Map<String, Object> session = new HashMap<String, Object>();
		try {
			byte[] bs = jedis.get(sid.getBytes());
			if (bs != null) {
				Object deserialize = Hessian2Serialization.deserialize(bs);
				session = (Map<String, Object>) deserialize;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return session;
	}

	public static void saveSession(String sid, Map<String, Object> session) {
		// getService().saveSession(sid, JSON.toJSONString(session));

		try {
			byte[] bs = Hessian2Serialization.serialize(session);
			jedis.set(sid.getBytes(), bs);
			jedis.expire(sid.getBytes(), 3600);// 设置过期时间
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void removeSession(String sid) {
		// getService().removeSession(sid);

		try {
			jedis.del(sid.getBytes());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void main(String[] args) {
		/*
		 * Jedis jedis = new Jedis("127.0.0.1", 6379); System.out.println(jedis);
		 * jedis.close();
		 */

	}

}