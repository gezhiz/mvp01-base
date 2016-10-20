package com.mvp01.service;

import com.mvp01.common.utils.LoggerWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Redis Service
 *
 * @author wenjie on 16/1/19.
 */
@Service
public class RedisService extends LoggerWrapper {
    private final static ConcurrentMap<String, Lock> LOCKS = new ConcurrentHashMap<String, Lock>();

    @Autowired
    private RedisTemplate redisTemplate;

    private RedisSerializer<String> serializer = new StringRedisSerializer();

    @PostConstruct
    public void init() {
        serializer = redisTemplate.getStringSerializer();
    }

    public long incr(final String key) {
        long rt = (long) redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.incr(serializer.serialize(key));
            }
        });
        return rt;
    }

    public long decr(final String key) {
        long rt = (long) redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.decr(serializer.serialize(key));
            }
        });
        return rt;
    }

    public long hIncrBy(final String key, final String field, final long value) {
        long rt = (long) redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.hIncrBy(serializer.serialize(key), serializer.serialize(field), value);
            }
        });
        return rt;
    }

    public void set(final String key, final String value) {
        redisTemplate.execute(new RedisCallback<Void>() {
            @Override
            public Void doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.set(serializer.serialize(key), serializer.serialize(value));
                return null;
            }
        });
    }

    public void setEx(final String key, final String value, final long expired) {
        redisTemplate.execute(new RedisCallback<Void>() {
            @Override
            public Void doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.setEx(serializer.serialize(key), expired, serializer.serialize(value));
                return null;
            }
        });
    }

    public boolean doJob(String key, Runnable task) {
        return doJob(key, 60000, task);
    }

    public boolean doJob(String key, long timeout, Runnable task) {
        if (StringUtils.isBlank(key)) {
            return true;
        }
        final long timestamp = System.currentTimeMillis() + timeout + 1;
        boolean lock = setNX(key, String.valueOf(timestamp));

        if (lock || (System.currentTimeMillis() > Long.parseLong(get(key)) && System.currentTimeMillis() > Long
                .parseLong(getSet(key, String.valueOf(timestamp))))) {
            LOGGER.info("开始执行任务：" + key);

            if (task != null) {
                task.run();
            }

            if (System.currentTimeMillis() < Long.parseLong(get(key))) {
                long delete = Long.parseLong(del(key));
            }
            LOGGER.info("任务执行完成：" + key);

            return true;
        } else {
            LOGGER.info("任务已经在执行中，本次不会执行： " + key);
            return false;
        }
    }

    public boolean setNX(final String key, final String value) {
        boolean t = (boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.setNX(serializer.serialize(key), serializer.serialize(value));
            }
        });
        return t;
    }

    public String getSet(final String key, final String value) {
        return (String) redisTemplate.boundValueOps(key).getAndSet(value);
    }

    public String get(final String key) {
        return (String) redisTemplate.boundValueOps(key).get();
    }

    public String del(final String key) {
        String t = (String) redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return String.valueOf(redisConnection.del(serializer.serialize(key)));
            }
        });
        return t;
    }

    public boolean expire(final String key, final long expired) {
        boolean t = (boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.expire(serializer.serialize(key), expired);
            }
        });
        return t;
    }

    public void doLock(String key, final Runnable task) {
        if (StringUtils.isBlank(key)) {
            return;
        }
        Lock lock = LOCKS.get(key);
        if (lock == null) {
            lock = new ReentrantLock();
            Lock oldLock = LOCKS.putIfAbsent(key, lock);
            if (oldLock != null) {
                lock = oldLock;
            }
        }
        lock.lock();
        try {
            while (!doJob(key, 10000, task)) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            lock.unlock();
        }

    }

}
