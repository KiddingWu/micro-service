package n.kidding.user.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * Redis 缓存配置类
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;

    //缓存管理器
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);

        //设置 CacheManager 的值序列化方式为 fastJsonRedisSerializer，但其实 RedisCacheConfiguration 默认使用 StringRedisSerializer 序列化key.
        ClassLoader loader = this.getClass().getClassLoader();

        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(loader.getClass());
        RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(fastJsonRedisSerializer);
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);
        RedisCacheManager cacheManager = new RedisCacheManager(redisCacheWriter, defaultCacheConfig);
        return cacheManager;
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate((factory));
        // 打开autotype功能
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        FastJsonRedisSerializer<Object> serializer = new FastJsonRedisSerializer<>(Object.class);
        template.setValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }

    private static class FastJsonRedisSerializer<T> implements RedisSerializer<T> {

        private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
        private Class<T> clazz;

        public FastJsonRedisSerializer(Class<T> clazz) {
            this.clazz = clazz;
        }

        @Override
        public byte[] serialize(T t) throws SerializationException {
            if (t == null) {
                return new byte[0];
            }
            return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes();
        }

        @Override
        public T deserialize(byte[] bytes) throws SerializationException {
            if (bytes == null || bytes.length == 0) {
                return null;
            }
            String str = new String(bytes, DEFAULT_CHARSET);
            return (T) JSON.parseObject(str, clazz);
        }
    }
}
