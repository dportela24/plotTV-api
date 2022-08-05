package com.dportela.plotTV.gateway

import com.dportela.plotTV.model.applicational.Series
import com.dportela.plotTV.model.exception.ConnectionErrorException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate

@Service
class ScrapperGateway(
    val scrapperRestTemplate: RestTemplate
) {
    val logger = LoggerFactory.getLogger(this::class.java)

    fun scrapSeriesById(imdbId: String) : Series {
        logger.info("Fetching series $imdbId")
        val series = try {
            scrapperRestTemplate
                .getForObject("/scrap/id/$imdbId", Series::class.java)
        } catch (ex: ResourceAccessException) {
            logger.error("Could not connect to scrapper. ${ex.message}")
            throw ConnectionErrorException("Could not connect to scrapper")
        }
        logger.info("Fetched series $imdbId")
        return series!!
    }

    fun scrapSeriesByName(name: String) : Series {
        logger.info("Fetching series with query $name")
        val series = try {
            scrapperRestTemplate
                .getForObject("/scrap/name/$name", Series::class.java)
        } catch (ex: ResourceAccessException) {
            logger.error("Could not connect to scrapper. ${ex.message}")
            throw ConnectionErrorException("Could not connect to scrapper")
        }
        logger.info("Fetched series with query $name")
        return series!!
    }
}