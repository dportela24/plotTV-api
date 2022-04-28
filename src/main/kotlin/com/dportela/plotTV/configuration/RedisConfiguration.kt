package com.dportela.plotTV.configuration

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.SingletonSupport
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfiguration(
) {

    @Bean
    fun redisConnectionFactory() : LettuceConnectionFactory {
        val clientConfig = LettuceClientConfiguration.defaultConfiguration()

        val redisStandaloneConfiguration = RedisStandaloneConfiguration("localhost", 6379)

        return LettuceConnectionFactory(
            redisStandaloneConfiguration,
            clientConfig
        )
    }

    @Bean
    fun redisTemplate(): RedisTemplate<*,*> {
        val mapper = ObjectMapper().apply {
            registerModule(KotlinModule(singletonSupport = SingletonSupport.CANONICALIZE))
            findAndRegisterModules()
            propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            visibilityChecker
                .withFieldVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.PUBLIC_ONLY)
            activateDefaultTypingAsProperty(
                BasicPolymorphicTypeValidator.builder()
                    .allowIfBaseType("java.")
                    .allowIfBaseType("kotlin.")
                    .allowIfSubType("java.")
                    .allowIfSubType("kotlin.")
                    .allowIfSubTypeIsArray()
                    .build(),
                ObjectMapper.DefaultTyping.EVERYTHING,
                "@class"
            )
        }


        val redisTemplate = RedisTemplate<ByteArray, ByteArray>()
        redisTemplate.setConnectionFactory(redisConnectionFactory())
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.hashKeySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = GenericJackson2JsonRedisSerializer(mapper)
        redisTemplate.hashValueSerializer = GenericJackson2JsonRedisSerializer(mapper)
        return redisTemplate
    }
}