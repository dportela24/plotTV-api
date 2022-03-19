package com.dportela.plotTV.configuration

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RestTemplateConfiguration {
    @Bean
    fun restTemplate() = RestTemplateBuilder()
        .rootUri("http://localhost:5000")
        .errorHandler(ScrapperRestTemplateResponseErrorHandler())
        .build()
}