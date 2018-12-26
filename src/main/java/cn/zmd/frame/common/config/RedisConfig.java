package cn.zmd.frame.common.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.core.JsonParser.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.support.NullValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;
import org.springframework.lang.Nullable;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.Duration;

/**
 * <p>类名：RedisConfig </p>
 * <p>说明 ：description of the class</p>
 * <p>创建日期： 2018年12月26日 12时00分</p>
 * <p>作者 ：mandy_choi</p>
 * <p>当前版本 ： 1.0</p>
 * <p>更新描述 ：   this description for update of the class</p>
 * <p>最后更新者 : mandy_choi</p>
 */
@Configuration
public class RedisConfig {
    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);
    @Value("${spring.cache.redis.time-to-live}")
    String redisTtl;
    @Value("${spring.cache.keyPrefix}")
    String prefix;

    public RedisConfig() {
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        RedisConfig.CustomJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new RedisConfig.CustomJackson2JsonRedisSerializer();
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        return redisTemplate;
    }

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        int ttl = (Integer) NumberUtils.parseNumber(this.redisTtl, Integer.class);
        if (ttl <= 0) {
            ttl = 86400;
        }

        this.prefix = StringUtils.isEmpty(this.prefix) ? "spr_" : this.prefix;
        logger.info("init redis configuration with prefix={} and ttl={}", this.prefix, ttl);
        return RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new RedisConfig.CustomJackson2JsonRedisSerializer())).entryTtl(Duration.ofSeconds((long)ttl)).computePrefixWith((cacheName) -> {
            return this.prefix + cacheName + (cacheName.endsWith("_") ? "" : "_");
        });
    }

    private class CustomJackson2JsonRedisSerializer implements RedisSerializer<Object> {
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer;

        public CustomJackson2JsonRedisSerializer() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
            mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
            mapper.registerModule((new SimpleModule()).addSerializer(new RedisConfig.CustomJackson2JsonRedisSerializer.NullValueSerializer((String)null)));
            mapper.enableDefaultTyping(DefaultTyping.NON_FINAL, As.PROPERTY);
            this.genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(mapper);
        }

        @Override
        public byte[] serialize(Object o) throws SerializationException {
            return this.genericJackson2JsonRedisSerializer.serialize(o);
        }

        @Override
        public Object deserialize(byte[] bytes) throws SerializationException {
            return this.genericJackson2JsonRedisSerializer.deserialize(bytes);
        }

        private class NullValueSerializer extends StdSerializer<NullValue> {
            private final String classIdentifier;

            NullValueSerializer(@Nullable String classIdentifier) {
                super(NullValue.class);
                this.classIdentifier = StringUtils.hasText(classIdentifier) ? classIdentifier : "@class";
            }

            @Override
            public void serialize(NullValue value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                jgen.writeStartObject();
                jgen.writeStringField(this.classIdentifier, NullValue.class.getName());
                jgen.writeEndObject();
            }
        }
    }
}
