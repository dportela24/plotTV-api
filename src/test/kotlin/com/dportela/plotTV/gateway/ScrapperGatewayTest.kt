package com.dportela.plotTV.gateway

import com.dportela.plotTV.configuration.RestTemplateConfiguration
import com.dportela.plotTV.configuration.ScrapperRestTemplateResponseErrorHandler.ScrapperErrorCodes
import com.dportela.plotTV.helper.generateImdbId
import com.dportela.plotTV.helper.generateScrapperErrorResponse
import com.dportela.plotTV.helper.generateSeries
import com.dportela.plotTV.model.exception.*
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.client.ExpectedCount
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.method
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withStatus
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate
import java.net.URI

@RunWith(SpringRunner::class)
@RestClientTest(ScrapperGateway::class)
@ImportAutoConfiguration(classes = [RestTemplateConfiguration::class])
class ScrapperGatewayTest {
    @Autowired
    lateinit var restTemplate: RestTemplate

    @Autowired
    lateinit var subject: ScrapperGateway

    @Autowired
    lateinit var mockServer: MockRestServiceServer

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun initMockServer() {
        mockServer = MockRestServiceServer.createServer(restTemplate)
    }

    fun setupMockServer(imdbId: String, httpStatus: HttpStatus, body: Any) {
        mockServer.expect(ExpectedCount.once(), requestTo(URI("http://localhost:8000/scrap/id/$imdbId")))
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(body)))
    }

    @Test
    fun `Happy path - series -If there's no errors or exceptions returns correct TV series`() {
        val imdbId = generateImdbId()
        val expectedSeries = generateSeries(imdbId)

        setupMockServer(imdbId, HttpStatus.OK, expectedSeries)

        val actualSeries = subject.fetchSeriesById(imdbId)

        assertEquals(expectedSeries, actualSeries)
    }

    @Test
    fun `Connection - If connection could not be established throws ConnectionErrorException`() {
        val imdbId = generateImdbId()
        val expectedErrorMessage = "Could not connect to scrapper"

        mockServer.expect(ExpectedCount.once(), requestTo(URI("http://localhost:8000/scrap/id/$imdbId")))
            .andExpect(method(HttpMethod.GET))
            .andRespond { _ -> throw ResourceAccessException("") }

        val ex = assertThrows<ConnectionErrorException> { subject.fetchSeriesById(imdbId) }

        assertTrue(ex.message.contains(expectedErrorMessage))
    }

    @Test
    fun `ScrapperErrors - TV Series not found - If not TV Series not found throws TVSeriesNotFoundException`() {
        val imdbId = generateImdbId()
        val scrapperErrorResponse = generateScrapperErrorResponse(ScrapperErrorCodes.TV_SERIES_NOT_FOUND)

        setupMockServer(imdbId, HttpStatus.NOT_FOUND, scrapperErrorResponse)

        val ex = assertThrows<TVSeriesNotFoundException> { subject.fetchSeriesById(imdbId) }

        assertEquals(scrapperErrorResponse.errorMessage, ex.message)
    }

    @Test
    fun `ScrapperErrors - Not a TV Series - If not a TV Series throws NotATVSeriesException`() {
        val imdbId = generateImdbId()
        val scrapperErrorResponse = generateScrapperErrorResponse(ScrapperErrorCodes.NOT_A_TV_SERIES_ERROR)

        setupMockServer(imdbId, HttpStatus.BAD_REQUEST, scrapperErrorResponse)

        val ex = assertThrows<NotATvSeriesException> { subject.fetchSeriesById(imdbId) }

        assertEquals(scrapperErrorResponse.errorMessage, ex.message)
    }

    @Test
    fun `ScrapperErrors - Invalid ImdbID - If invalid ImdbId throws InvalidImdbIdException`() {
        val imdbId = generateImdbId()
        val scrapperErrorResponse = generateScrapperErrorResponse(ScrapperErrorCodes.INVALID_IMDB_ID)

        setupMockServer(imdbId, HttpStatus.BAD_REQUEST, scrapperErrorResponse)

        val ex = assertThrows<InvalidImdbIdException> { subject.fetchSeriesById(imdbId) }

        assertEquals(scrapperErrorResponse.errorMessage, ex.message)
    }

    @Test
    fun `ScrapperErrors - Connection Error - If scrapper had a connection error throws ScrappingUnavailableException`() {
        val imdbId = generateImdbId()
        val scrapperErrorResponse = generateScrapperErrorResponse(
            ScrapperErrorCodes.CONNECTION_ERROR,
            "Could not retrieve series due to scrapper being down..."
        )

        setupMockServer(imdbId, HttpStatus.BAD_GATEWAY, scrapperErrorResponse)

        val ex = assertThrows<ScrappingUnavailableException> { subject.fetchSeriesById(imdbId) }

        assertEquals(scrapperErrorResponse.errorMessage, ex.message)
    }

    @Test
    fun `ScrapperErrors - Scrapping Error - If scrapper could not scrap throws ScrappingUnavailableException`() {
        val imdbId = generateImdbId()
        val scrapperErrorResponse = generateScrapperErrorResponse(
            ScrapperErrorCodes.SCRAPPING_ERROR,
            "Could not retrieve series due to scrapper being down..."
        )
        setupMockServer(imdbId, HttpStatus.SERVICE_UNAVAILABLE, scrapperErrorResponse)

        val ex = assertThrows<ScrappingUnavailableException> { subject.fetchSeriesById(imdbId) }

        assertEquals(scrapperErrorResponse.errorMessage, ex.message)
    }

    @Test
    fun `ScrapperErrors - Unexpected Error - If scrapper had an unexpected error throws ScrappingUnavailableException`() {
        val imdbId = generateImdbId()
        val scrapperErrorResponse = generateScrapperErrorResponse(
            ScrapperErrorCodes.UNEXPECTED_ERROR,
            "Could not retrieve series due to scrapper being down..."
        )
        setupMockServer(imdbId, HttpStatus.SERVICE_UNAVAILABLE, scrapperErrorResponse)

        val ex = assertThrows<ScrappingUnavailableException> { subject.fetchSeriesById(imdbId) }

        assertEquals(scrapperErrorResponse.errorMessage, ex.message)
    }
}