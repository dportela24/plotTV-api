package com.dportela.plotTV.service

import com.dportela.plotTV.gateway.ScrapperGateway
import com.dportela.plotTV.helper.generateImdbId
import com.dportela.plotTV.helper.generateName
import com.dportela.plotTV.helper.generateSeries
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SeriesServiceTest {
    val repositoryService = mockk<RepositoryService>()

    val scrapperGateway = mockk<ScrapperGateway>()

    val subject = SeriesService(repositoryService, scrapperGateway)

    @Test
    fun `ById - Happy Path - If series exists on database returns it and does not use scrapper`() {
        val imdbId = generateImdbId()
        val expectedSeries = generateSeries(imdbId)

        every { repositoryService.findSeriesByImdbId(imdbId) } returns expectedSeries

        val actualSeries = subject.getSeriesById(imdbId)

        verify(exactly = 1) { repositoryService. findSeriesByImdbId(imdbId) }
        verify{ scrapperGateway wasNot Called}
        assertEquals(expectedSeries, actualSeries)
    }

    @Test
    fun `ById - Happy Path - If series does not exist on database, requests scrapper to scrap it, and saves it on database`(){
        val imdbId = generateImdbId()
        val expectedSeries = generateSeries(imdbId)

        every { repositoryService.findSeriesByImdbId(imdbId) } returns null
        every { scrapperGateway.scrapSeriesById(imdbId) } returns expectedSeries
        every { repositoryService.saveSeries(expectedSeries) } returns Unit

        val actualSeries = subject.getSeriesById(imdbId)

        verify(exactly = 1) { repositoryService.findSeriesByImdbId(imdbId) }
        verify(exactly = 1) { scrapperGateway.scrapSeriesById(imdbId) }
        verify(exactly = 1) { repositoryService.saveSeries(expectedSeries) }
        assertEquals(expectedSeries, actualSeries)
    }

    @Test
    fun `ByName - Happy Path - If series does not exist in database, requests scrapper to scrap it, and saves it on database`(){
        val name = generateName()
        val imdbId = generateImdbId()
        val expectedSeries = generateSeries(imdbId = imdbId, name = name)

        every { repositoryService.findSeriesByName(name) } returns null
        every { scrapperGateway.scrapSeriesByName(name) } returns expectedSeries
        every { repositoryService.existsByImdbId(imdbId) } returns false
        every { repositoryService.saveSeries(expectedSeries) } returns Unit

        val actualSeries = subject.getSeriesByName(name)

        verify(exactly = 1) { repositoryService.findSeriesByName(name) }
        verify(exactly = 1) { scrapperGateway.scrapSeriesByName(name) }
        verify(exactly = 1) { repositoryService.existsByImdbId(imdbId) }
        verify(exactly = 1) { repositoryService.saveSeries(expectedSeries) }
        assertEquals(expectedSeries, actualSeries)
        confirmVerified(repositoryService, scrapperGateway)
    }

    @Test
    fun `ByName - Happy Path - If series exists in database but cannot find it by name, requests scrapper to scrap it, but does not save it on database`(){
        val name = generateName()
        val imdbId = generateImdbId()
        val expectedSeries = generateSeries(imdbId = imdbId, name = name)

        every { repositoryService.findSeriesByName(name) } returns null
        every { scrapperGateway.scrapSeriesByName(name) } returns expectedSeries
        every { repositoryService.existsByImdbId(imdbId) } returns true
        every { repositoryService.saveSeries(expectedSeries) } returns Unit

        val actualSeries = subject.getSeriesByName(name)

        verify(exactly = 1) { repositoryService.findSeriesByName(name) }
        verify(exactly = 1) { scrapperGateway.scrapSeriesByName(name) }
        verify(exactly = 1) { repositoryService.existsByImdbId(imdbId) }
        assertEquals(expectedSeries, actualSeries)
        confirmVerified(repositoryService, scrapperGateway)
    }
}