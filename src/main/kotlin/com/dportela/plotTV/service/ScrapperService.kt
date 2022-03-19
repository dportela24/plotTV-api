package com.dportela.plotTV.service

import com.dportela.plotTV.model.Series
import com.dportela.plotTV.model.exception.ConnectionErrorException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate
import kotlin.math.log

@Service
class ScrapperService(
    val scrapperRestTemplate: RestTemplate
) {
    val logger = LoggerFactory.getLogger(this::class.java)

    fun fetchSeries(imdbId: String) : Series? {
        logger.info("Fetching series $imdbId")
        val response = try {
            scrapperRestTemplate
                .getForEntity("/scrap/id/$imdbId", Series::class.java)
        } catch (ex: ResourceAccessException) {
            logger.error("Could not connect to scrapper. ${ex.message}")
            throw ConnectionErrorException("Could not connect to scrapper")
        }
        logger.info("Fetched series $imdbId")
        return response.body
    }
}