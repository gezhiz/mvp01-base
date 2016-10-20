package com.mvp01.service;

import com.mvp01.common.exception.ParamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RedisLockService {
    //加锁标志
    public static final String LOCKED = "TRUE";
    //纳秒单位转换
    public static final long ONE_MILLI_NANOS = 1000000L;
    //默认超时时间（毫秒）
    public static final long DEFAULT_TIME_OUT = 30000;
    //锁的超时时间（秒），过期删除
    public static final int EXPIRE = 60;
    public static final Random r = new Random();
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RedisService redisService;
    //锁状态标志
    private boolean locked = false;

    public RedisLockService() {
    }

    public boolean lock(String key) {
        return lock(key, DEFAULT_TIME_OUT);
    }

    public boolean lock(String key, long timeout) {
        long nano = System.nanoTime();
        timeout *= ONE_MILLI_NANOS;
        try {
            while ((System.nanoTime() - nano) < timeout) {
                if (redisService.setNX(key, LOCKED)) {
                    redisService.expire(key, EXPIRE);
                    locked = true;
                    logger.info("start lock key [" + key + "]");
                    return locked;
                }
                // 短暂休眠，nano避免出现活锁
                Thread.sleep(3, r.nextInt(500));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ParamException("服务器正在维护");
        }
        return false;
    }

    // 无论是否加锁成功，必须调用
    public void unlock(String key) {
        if (locked) {
            logger.info("finish lock key [" + key + "]");
            redisService.del(key);
        }
    }
}
