package com.dportela.plotTV.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.time.temporal.TemporalAccessor


@Configuration
class JacksonConfiguration {
    fun jacksonObjectMapper() : ObjectMapper {
        val objectMapper = ObjectMapper()
        val module = SimpleModule()
        module.addDeserializer(TemporalAccessor::class.java, TemporalAccessorDeserializer())
        objectMapper.registerModules(module, JavaTimeModule())
        return objectMapper
    }

    fun objectMapperBuilder(): Jackson2ObjectMapperBuilder? {
        // Configure the builder to suit your needs
        return Jackson2ObjectMapperBuilder().deserializers(TemporalAccessorDeserializer()).modules(JavaTimeModule())
    }
}