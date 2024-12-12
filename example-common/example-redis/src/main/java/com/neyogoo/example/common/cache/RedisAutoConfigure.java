package com.neyogoo.example.common.cache;

import com.neyogoo.example.common.cache.lock.DistributedLock;
import com.neyogoo.example.common.cache.lock.RedisDistributedLock;
import com.neyogoo.example.common.cache.properties.MyRedisProperties;
import com.neyogoo.example.common.cache.redis.RedisObjectSerializer;
import com.neyogoo.example.common.cache.redis.RedisOps;
import com.neyogoo.example.common.cache.repository.CacheOps;
import com.neyogoo.example.common.cache.repository.impl.RedisOpsImpl;
import com.neyogoo.example.common.core.util.StrPool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Objects;

import static com.neyogoo.example.common.core.constant.BasicConstant.REDIS_PREFIX;


/**
 * redis 配置类
 */
@Slf4j
@ConditionalOnClass(RedisConnectionFactory.class)
@ConditionalOnProperty(prefix = REDIS_PREFIX, name = "enabled", havingValue = "true",
        matchIfMissing = true)
@EnableConfigurationProperties({RedisProperties.class, MyRedisProperties.class})
@RequiredArgsConstructor
public class RedisAutoConfigure {

    private final MyRedisProperties cacheProperties;

    /**
     * 分布式锁
     *
     * @return 分布式锁
     */
    @Bean
    @ConditionalOnMissingBean
    public DistributedLock redisDistributedLock(
            @Qualifier("redisTemplate") RedisTemplate<String, Object> redisTemplate) {
        return new RedisDistributedLock(redisTemplate);
    }

    /**
     * RedisTemplate配置
     *
     * @param factory redis链接工厂
     */
    @Bean("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory,
                                                       RedisSerializer<Object> redisSerializer) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        setSerializer(factory, template, redisSerializer);
        return template;
    }

    @Bean
    @ConditionalOnMissingBean(RedisSerializer.class)
    public RedisSerializer<Object> redisSerializer() {
        return new RedisObjectSerializer();
    }

    private void setSerializer(RedisConnectionFactory factory, RedisTemplate template,
                               RedisSerializer<Object> redisSerializer) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(redisSerializer);
        template.setValueSerializer(redisSerializer);
        template.setConnectionFactory(factory);
    }

    @Bean("stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(factory);
        return template;
    }

    /**
     * redis 持久库
     */
    @Bean
    @ConditionalOnMissingBean
    public CacheOps cacheOps(RedisOps redisOps) {
        return new RedisOpsImpl(redisOps);
    }

    /**
     * 用于 @Cacheable 相关注解
     *
     * @param redisConnectionFactory 链接工厂
     * @return 缓存管理器
     */
    @Bean(name = "cacheManager")
    @Primary
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration defConfig = getDefConf();
        defConfig.entryTtl(cacheProperties.getDef().getTimeToLive());
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defConfig)
                .build();
    }

    private RedisCacheConfiguration getDefConf() {
        RedisCacheConfiguration def = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new RedisObjectSerializer()));
        return handleRedisCacheConfiguration(cacheProperties.getDef(), def);
    }

    private RedisCacheConfiguration handleRedisCacheConfiguration(MyRedisProperties.Cache redisProperties,
                                                                  RedisCacheConfiguration config) {
        if (Objects.isNull(redisProperties)) {
            return config;
        }
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        config = config.computePrefixWith(cacheName -> cacheName.concat(StrPool.COLON));
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        config = config.disableKeyPrefix();
        return config;
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisOps getRedisOps(@Qualifier("redisTemplate") RedisTemplate<String, Object> redisTemplate,
                                StringRedisTemplate stringRedisTemplate) {
        return new RedisOps(redisTemplate, stringRedisTemplate, cacheProperties.getCacheNullVal());
    }
}
