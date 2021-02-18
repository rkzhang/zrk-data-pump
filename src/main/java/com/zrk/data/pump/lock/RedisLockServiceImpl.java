package com.zrk.data.pump.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.google.common.base.Objects;
import com.zrk.data.pump.utils.LogUtil;

@Service("redisLockService")
public class RedisLockServiceImpl implements ILockService {

	private final Logger log = LoggerFactory.getLogger(RedisLockServiceImpl.class);
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	private static final String KEY_PRE = "dk:";

	@Override
	public int queryStock(String code) {
		return Integer.parseInt(redisTemplate.opsForValue().get(code));
	}

	public boolean isSuccessful(Map<String, String> result) {
		return result != null && Objects.equal("true", result.get("lock"));
	}

	public String lock(String lockKey, int timeout, int expire) {
		Map<String, String> result = lock(lockKey, timeout * 1000l, expire * 1000l);
		if (!isSuccessful(result)) {
			throw new RuntimeException("未能获取锁");
		}
		return result.get("lockTime");
	}

	public Map<String, String> lock(String key, long timeout, long expire) {
		Map<String, String> map = new HashMap<>();
		String lockKey = KEY_PRE + key;
		long start = System.currentTimeMillis();
		try {
			map = new HashMap<>();
			while ((System.currentTimeMillis() - start) <= timeout) {
				String oldTime = String.valueOf((System.currentTimeMillis() + expire));
				BoundValueOperations<String, String> opt = redisTemplate.boundValueOps(lockKey);
				boolean setnx = opt.setIfAbsent(oldTime, expire, TimeUnit.MILLISECONDS);
				if (setnx) {
					log.debug("{} 加锁成功 key --- {}", Thread.currentThread().getName(), lockKey);					
					map.put("lock", "true");
					map.put("lockTime", oldTime);
					return map;
				} else {
					// 加锁失败 虽然失败 但有可能是因为持有锁的进程宕机了 未设置失效时间 导致锁一直被占用
					String lockValue = opt.get();
					if (lockValue != null && Long.parseLong(lockValue) < System.currentTimeMillis()) {
						// 说明锁已经过期 但因为持有者错误 导致未设置过期时间
						String oldExpireTime = opt.getAndSet(oldTime);
						if (oldExpireTime != null && oldExpireTime.equals(lockValue)) {// 获得超时锁
							opt.expire(expire, TimeUnit.MILLISECONDS); // 设置过期时间，防止在进程宕机时，锁会释放
							log.error("{} 获得超时锁加锁成功", Thread.currentThread().getName());
							map.put("lock", "true");
							map.put("lockTime", oldTime);
							return map;
						}
					}
					Thread.sleep(200);
				}
			}
		} catch (Exception e) {
			LogUtil.logErrorStackMsg(log, e);
		}
		return map;
	}

	public void unlock(String key, String oldCurrentTime) {
		String lockKey = KEY_PRE + key;
		try {
			BoundValueOperations<String, String> opt = redisTemplate.boundValueOps(lockKey);
			String redisOldCurrentTime = opt.get();
			if (redisOldCurrentTime != null && redisOldCurrentTime.equals(oldCurrentTime)) {
				redisTemplate.delete(lockKey);
				log.debug("{} 有权限解锁 释放锁 key --- {}", Thread.currentThread().getName(), lockKey);
			}
		} catch (Exception e) {
			LogUtil.logErrorStackMsg(log, e);
		}

	}
}
