package com.dportela.plotTV.service

import com.dportela.plotTV.gateway.ScrapperGateway
import com.dportela.plotTV.helper.validateImdbId
import com.dportela.plotTV.model.dao.TitleMatchingCacheDAO
import com.dportela.plotTV.model.applicational.Series
import com.dportela.plotTV.repository.QueryCache
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.slf4j.MDCContext
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SeriesService(
    val repositoryService: RepositoryService,
    val queryCache: QueryCache,
    val scrapperGateway: ScrapperGateway
) {
    val logger = LoggerFactory.getLogger(this::class.java)

    fun getSeries(query: String): Series {
        logger.info("Getting series with query $query")

        return queryCache.get(query)?.let { cachedImdbId ->
            repositoryService.findSeriesByImdbId(cachedImdbId)
        } ?: let {
            logger.info("Could not find series with requested name $query. Going to fetch it.")
            scrapSeries(query)
        }
    }

    fun scrapSeries(query: String) : Series {
        val series =
            if (validateImdbId(query))
                scrapSeriesById(query)
            else
                scrapSeriesByName(query)

        runBlocking {
            saveSeriesData(query, series)
        }

        return series
    }

    fun scrapSeriesByName(name: String) : Series {
        return scrapperGateway.scrapSeriesByName(name).also { scrappedSeries ->
            runBlocking(MDCContext()) {
               saveSeriesData(name, scrappedSeries)
            }
        }
    }

    fun scrapSeriesById(id: String) : Series {
        return scrapperGateway.scrapSeriesById(id).also { scrappedSeries ->
            runBlocking(MDCContext()) {
                saveSeriesData(id, scrappedSeries)
            }
        }
    }

    private fun saveSeriesData(inputQuery: String, seriesToSave: Series) {
        if (!repositoryService.existsByImdbId(seriesToSave.imdbId)) {
            logger.info("Series does not exist on database. Going to save it.")
            repositoryService.saveSeries(seriesToSave)
        }

        queryCache.save(
            inputQuery,
            seriesToSave.imdbId
        )
    }
}