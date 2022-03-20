package com.dportela.plotTV.service

import com.dportela.plotTV.gateway.ScrapperGateway
import com.dportela.plotTV.model.Series
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SeriesService(
    val repositoryService: RepositoryService,
    val scrapperGateway: ScrapperGateway
) {
    val logger = LoggerFactory.getLogger(this::class.java)

    fun getSeries(imdbId: String): Series? {
        logger.info("Getting series $imdbId")
        val seriesDAO = repositoryService.findSeriesByImdbId(imdbId)

        return seriesDAO ?: createSeries(imdbId)
    }

    private fun createSeries(imdbId: String): Series? {
        logger.info("Series $imdbId does not exist. Going to fetch it.")
        return scrapperGateway.fetchSeries(imdbId)?.also { repositoryService.saveSeries(it) }
    }
}