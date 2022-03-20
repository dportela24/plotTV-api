package com.dportela.plotTV.service

import com.dportela.plotTV.gateway.ScrapperGateway
import com.dportela.plotTV.helper.generateImdbId
import com.dportela.plotTV.helper.generateSeries
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.exp

class SeriesServiceTest {
    val repositoryService = mockk<RepositoryService>()

    val scrapperGateway = mockk<ScrapperGateway>()

    val subject = SeriesService(repositoryService, scrapperGateway)

    @Test
    fun `Happy Path - If series exists on database returns it and does not use scrapper`() {
        val imdbId = generateImdbId()
        val expectedSeries = generateSeries(imdbId)

        every { repositoryService.findSeriesByImdbId(imdbId) } returns expectedSeries

        val actualSeries = subject.getSeries(imdbId)

        verify(exactly = 1) { repositoryService. findSeriesByImdbId(imdbId) }
        verify{ scrapperGateway wasNot Called}
        assertEquals(expectedSeries, actualSeries)
    }

    @Test
    fun `Happy Path - If series does not exist on database, requests scrapper to scrap it, and saves it on database`(){
        val imdbId = generateImdbId()
        val expectedSeries = generateSeries(imdbId)

        every { repositoryService.findSeriesByImdbId(imdbId) } returns null
        every { scrapperGateway.fetchSeries(imdbId) } returns expectedSeries
        every { repositoryService.saveSeries(expectedSeries) } returns Unit

        val actualSeries = subject.getSeries(imdbId)

        verify(exactly = 1) { repositoryService.findSeriesByImdbId(imdbId) }
        verify(exactly = 1) { scrapperGateway.fetchSeries(imdbId) }
        verify(exactly = 1) { repositoryService.saveSeries(expectedSeries) }
        assertEquals(expectedSeries, actualSeries)
    }
}