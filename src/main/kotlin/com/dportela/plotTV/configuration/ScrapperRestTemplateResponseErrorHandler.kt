package com.dportela.plotTV.configuration

import com.dportela.plotTV.model.ErrorDetails
import com.dportela.plotTV.model.exception.ParsingErrorException
import com.dportela.plotTV.model.exception.TVSeriesNotFoundException
import com.dportela.plotTV.model.exception.NotATvSeriesException
import com.dportela.plotTV.model.exception.InvalidImdbIdException
import com.dportela.plotTV.model.exception.ScrappingUnavailableException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.DefaultResponseErrorHandler
import java.net.URI

class ScrapperRestTemplateResponseErrorHandler : DefaultResponseErrorHandler() {
    val logger = LoggerFactory.getLogger(this::class.java)

    override fun handleError(url: URI, method: HttpMethod, response: ClientHttpResponse) {
        val errorDetails = try {
            jacksonObjectMapper().readValue(response.body, ErrorDetails::class.java)
        }catch (ex: Exception) {
            val bodyText = String(response.body.readBytes())
            logger.error("Could not parse scrapper's errorDetails. Body: $bodyText")
            throw ParsingErrorException("Could not parse scrapper's error")
        }

        when (errorDetails.errorCode) {
            ScrapperErrorCodes.NOT_A_TV_SERIES_ERROR -> throw NotATvSeriesException(errorDetails.errorMessage)
            ScrapperErrorCodes.INVALID_IMDB_ID -> throw InvalidImdbIdException(errorDetails.errorMessage)
            ScrapperErrorCodes.NO_SEARCH_RESULTS,
            ScrapperErrorCodes.TV_SERIES_NOT_FOUND -> throw TVSeriesNotFoundException(errorDetails.errorMessage)
            ScrapperErrorCodes.SCRAPPING_ERROR,
            ScrapperErrorCodes.UNEXPECTED_ERROR -> {
                logger.error("An error occurred while scrapping title. ${errorDetails.errorMessage}")
                throw ScrappingUnavailableException("Could not scrap data... You sure title is a tv series?")
            }
            ScrapperErrorCodes.CONNECTION_ERROR -> {
                logger.error("Could not connect to scrapper. ${errorDetails.errorMessage}")
                throw ScrappingUnavailableException("Could not retrieve series due to scrapper being down...")
            }
            else -> {
                logger.error("Unknown error response for scrapper. ErrorDetails = $errorDetails")
                throw RuntimeException("Unknown error from scrapper")
            }
        }
    }

    class ScrapperErrorCodes {
        companion object {
            const val NOT_A_TV_SERIES_ERROR = "000003"
            const val CONNECTION_ERROR      = "000004"
            const val SCRAPPING_ERROR       = "000005"
            const val INVALID_IMDB_ID       = "000006"
            const val TV_SERIES_NOT_FOUND   = "000007"
            const val NO_SEARCH_RESULTS     = "000009"
            const val UNEXPECTED_ERROR      = "111111"
        }
    }
}