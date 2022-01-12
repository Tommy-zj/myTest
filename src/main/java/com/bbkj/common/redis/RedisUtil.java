package com.bbkj.common.redis;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


/**
 * @author JJ
 * @version 1.0
 * @description: TODO
 * @date 2022/1/5 16:17
 */
@Component("redisUtil")
public class RedisUtil {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public void put(String key, String value) {
        if (key == null || "".equals(key)) {
            return;
        }
        redisTemplate.opsForHash().put(key, key, value);
    }


    public void set(String key, String value) {
        if (key == null || "".equals(key)) {
            return;
        }
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, String value, long timeout) {
        if (key == null || "".equals(key)) {
            return;
        }
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    public void set(String key, String value, long timeout, String TimeType) {

        if (key == null || "".equals(key)) {
            return;
        }
        if (TimeType == "s") {
            redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
        }
        if (TimeType == "h") {
            redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.HOURS);
        }
        if (TimeType == "d") {
            redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.DAYS);
        }

    }


    public String get(String key) {
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj == null) {
            return null;
        } else {
            return String.valueOf(obj);
        }
    }

}
