package com.dportela.plotTV.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RestTemplateConfiguration(
    @Value("http://localhost:5000") val scrapperURL: String
) {
    @Bean
    fun restTemplate() = RestTemplateBuilder()
        .rootUri(scrapperURL)
        .errorHandler(ScrapperRestTemplateResponseErrorHandler())
        .build()
}