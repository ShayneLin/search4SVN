package com.shark.search4SVN.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;


/**
 * Created by nowcoder on 2016/7/30.
 */
@Component
@Scope("prototype")
public class JedisAdapter implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool pool;

    @Autowired
    private RedisProperties redisProperties;


    @Override
    public void afterPropertiesSet() throws Exception {

        String url = redisProperties.url;
        String db = redisProperties.db;

        logger.debug(String.format("redis locations: %s, db: %s", url, db));

        String address = url + db;

        pool = new JedisPool(address);
    }

    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public String spop(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.spop(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }

    @Component
    @ConfigurationProperties(prefix = "search4SVN.redis", locations = "classpath:config/search4SVN.properties")
    static class RedisProperties{
        private String url;
        private String db;

        public void setUrl(String url) {
            this.url = url;
        }

        public void setDb(String db) {
            this.db = db;
        }
    }
}
