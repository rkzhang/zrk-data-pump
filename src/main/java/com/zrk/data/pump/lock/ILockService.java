package com.zrk.data.pump.lock;

import java.util.Map;

public interface ILockService {

    int queryStock(String code);
    
    /**
     * 加锁
     * @param lockKey 锁的键
     * @param timeout 等待锁的超时时间 秒
     * @param pexpire 锁的键的过期时间 秒
     * @return
     */
    String lock(String lockKey, int timeout, int expire);

    /**
     * 加锁
     * @param lockKey 锁的键
     * @param timeout 等待锁的超时时间 毫秒
     * @param pexpire 锁的键的过期时间  毫秒
     * @return
     */
    Map<String,String> lock(String lockKey, long timeout, long pexpire);

    /**
     * 解锁
     * @param lockKey 锁的键
     * @param oldCurrentTime
     */
    void unlock(String lockKey, String oldCurrentTime);
}
