package com.umbookings.config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Shrikar Kalagi
 *
 */
@Configuration
public class RedisConfig {
	@Value("${spring.redis.host}")
	private String redisHost;
	
	@Value("${spring.redis.port}")
	private Integer redisPort;
	private static final Logger LOG = LoggerFactory.getLogger(RedisConfig.class);

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		
		LOG.info("Redis Connecting on host {} port{}",redisHost,redisPort);
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisHost, redisPort);
	    
	    return new JedisConnectionFactory(redisStandaloneConfiguration);
	    
	}

	@Bean
	RedisTemplate< String, Object> redisTemplate() {
	     
		LOG.info("Redis Connecting on host {} port {}",redisHost,redisPort);
	   	final RedisTemplate< String, Object > template =  new RedisTemplate<>();
	     
	    template.setConnectionFactory( jedisConnectionFactory() );
	    template.setKeySerializer( new StringRedisSerializer() );
	    template.setHashValueSerializer( new GenericToStringSerializer< Object >( Object.class ) );
	    template.setValueSerializer( new GenericJackson2JsonRedisSerializer() );
	    return template;
	}
}
