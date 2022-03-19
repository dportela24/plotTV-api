package com.dportela.plotTV.service

import com.dportela.plotTV.model.Series
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SeriesService(
    val repositoryService: RepositoryService,
    val scrapperService: ScrapperService
) {
    val logger = LoggerFactory.getLogger(this::class.java)

    fun createSeries(imdbId: String): Series? {
        return scrapperService.fetchSeries(imdbId)?.also { repositoryService.saveSeries(it) }
    }

    fun getSeries(imdbId: String): Series? {
        val seriesDAO = repositoryService.findSeriesByImdbId(imdbId)

        return seriesDAO ?: createSeries(imdbId)
    }
}